package com.vellkare.core

import com.vellkare.api.*
import grails.rest.RestfulController
import grails.transaction.Transactional
import io.swagger.annotations.*
import org.springframework.web.multipart.commons.CommonsMultipartFile

import javax.imageio.ImageIO
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import java.awt.image.BufferedImage

import static java.awt.RenderingHints.KEY_INTERPOLATION
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC

class MediaDetails {
  int width
  int height
  String mediatype

  MediaDetails(int nwidth, int nheight, String type) {
    width = nwidth
    height = nheight
    mediatype = type
  }
}

class MediaFileDetails {
  String mediapath
  String mediatype
}

class MediaInfo {
  String extension
  String supportedsizes
  List<MediaFileDetails> mediaFileDetailsList
}

@Path("/v0/media")
@Api(value = "media", description = "Operations about Media")
@Produces(["application/json", "application/xml"])
class MediaController extends RestfulController {
  static responseFormats = ['json', 'xml']
  static namespace = 'v0'

  //getMedia for getting all media of an entity

  //@Secured(["hasRole('ROLE_USER')"])
  @GET
  @Path("/")
  @ApiOperation(value = "Media get", notes = "This is used to get media", tags = ["/v0/media", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "User loggedin successfully", response = MediaApiModel.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = NotFoundResponse.class, responseContainer = "List"), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = MediaErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(name = "id", value = "Id", dataType = "long", paramType = "query"),
    @ApiImplicitParam(name = "mediafor", value = "mediafor", dataType = "string", paramType = "query")])
  @Transactional
  def getMedia(Long id, String mediafor) {
    def mediaConfig = grailsApplication.config.vellkare.media."${mediafor}"
    def className = mediaConfig.className
    if (!className) {
      respond new NotFoundResponse("Unsupported media: ${mediafor}", [])
      return
    }
    Class aClass = grailsApplication.getDomainClass(className)?.clazz
    def obj = aClass.get(id)
    if (!obj) {
      respond new NotFoundResponse("${id} for ${mediafor} Not Found.", [id])
      return
    }
    try {
      Media media = obj.getLatestMedia()
      def location = media ? media.location : mediaConfig.defaultMedia
      def extension = media ? media.extension : mediaConfig.defaultExtension
      def pictureTypes = media ? media.supportedTypes() : Media.defaultSupportedTypes(mediaConfig)
      def mediaInfo = new MediaInfo(extension: extension, supportedsizes: pictureTypes.collect { k, v -> k + ":" + v[0] + "x" + v[1] }.join(","), mediaFileDetailsList: [])
      pictureTypes.each { mediatype, size ->             //mediatype is small, size is array [48,48]
        mediaInfo.mediaFileDetailsList << new MediaFileDetails(mediapath: grailsApplication.config.grails.serverURL ? grailsApplication.config.grails.serverURL + "${location}_${size[0]}x${size[1]}.${extension}" : 'http://localhost:8080/' + grailsApplication.metadata.'app.name' + "${location}_${size[0]}x${size[1]}.${extension}", mediatype: mediatype)
      }
      mediaInfo.mediaFileDetailsList << new MediaFileDetails(mediapath: grailsApplication.config.grails.serverURL ? grailsApplication.config.grails.serverURL + "${location}.${extension}" : 'http://localhost:8080/' + grailsApplication.metadata.'app.name' + "${location}.${extension}", mediatype: "original")
      respond new MediaApiModel(extension: extension, supportedsizes: pictureTypes.collect { k, v -> k + ":" + v[0] + "x" + v[1] }.join(","), mediaFileDetailsList: mediaInfo.mediaFileDetailsList)
      return
    } catch (Exception ex) {
      respond new MediaErrorResponse("Problem in media Download", [])
      return
    }
  }

  private void storePicture(String profilePath, File myFile, CommonsMultipartFile photoFile,
                            MediaDetails mediaDetails, String picturetype, String extension) {
    def img = ImageIO.read(myFile)
    new BufferedImage(mediaDetails.width, mediaDetails.height, img.type).with { i ->
      createGraphics().with {
        setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC)
        drawImage(img, 0, 0, mediaDetails.width, mediaDetails.height, null)
        dispose()
      }
      String originalFilename = photoFile.originalFilename.replaceAll("\\s", "")
      String filename = originalFilename.contains('.') ? originalFilename.split("\\.")[0] : originalFilename
      ImageIO.write(i, extension, new File(servletContext.getRealPath(profilePath + "/" + filename +
        "_" + picturetype + "." + extension)))
    }
  }

  //@Secured(["hasRole('ROLE_USER')"])
  @POST
  @Path("/")
  @ApiOperation(value = "Media upload", notes = "This is used to upload media", tags = ["/v0/media", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "User loggedin successfully", response = SuccessResponse.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = ValidationErrorResponse.class, responseContainer = "List"), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(name = "id", value = "Id", dataType = "long", paramType = "query"),
    @ApiImplicitParam(name = "mediafor", value = "mediafor", dataType = "string", paramType = "query"),
    @ApiImplicitParam(name = "file", value = "file", dataType = "file", paramType = "formData")])
  @Transactional
  def saveMedia(Long id, String mediafor) {
    try {
      def mediaConfig = grailsApplication.config.vellkare.media."${mediafor}"
      def className = mediaConfig.className
      if (!className) {
        respond new NotFoundResponse("Unsupported mediafor ${mediafor}.")
        return
      }
      Class aClass = grailsApplication.getDomainClass(className).clazz
      def obj = aClass.get(id)
      if (!obj) {
        respond new NotFoundResponse("id for ${mediafor} Not Found.", [id])
        return
      }
      def mediaPath = mediaConfig.path + "" + id
      CommonsMultipartFile photoFile = request.getFile('file')
      def userDir = new File(servletContext.getRealPath(mediaPath))
      userDir.mkdirs()
      File myFile = new File(userDir, photoFile.originalFilename)
      photoFile.transferTo(myFile)
      def originalFilename = photoFile.originalFilename.replaceAll("\\s", "")
      Media media
      if (originalFilename.contains('.')) {
        media = new Media(extension: originalFilename.split('\\.')[1], fileName: originalFilename.split("\\.")[0], location: mediaPath + "/" + originalFilename.split("\\.")[0])
      } else {
        media = new Media(extension: mediaConfig.defaultExtension, fileName: originalFilename, location: mediaPath + "/" + originalFilename)
      }
      String supportedPictureTypes = ""
      def pictureTypes = Media.defaultSupportedTypes(mediaConfig)
      pictureTypes.each { mediatype, size ->
        def img = ImageIO.read(myFile)
        def imgWidth = img.getWidth(), imgHeight = img.getHeight()
        def max_width = size[0], max_height = size[1], ratio, new_width, new_height
        //determine which side is the longest to use in calculating length of the shorter side, since the longest will be the max size for whichever side is longest.
        if (imgHeight > imgWidth) {
          ratio = max_height / imgHeight
          new_height = max_height
          new_width = imgWidth * ratio
        } else {
          ratio = max_width / imgWidth
          new_width = max_width
          new_height = imgHeight * ratio
          if (new_height > max_height) {
            ratio = max_height / imgHeight
            new_height = max_height
            new_width = imgWidth * ratio
          }
        }
        String picturesize = max_width + "x" + max_height
        storePicture(mediaPath, myFile, photoFile, new MediaDetails(new_width.intValue(), new_height.intValue(), mediatype), picturesize, media.extension)
        supportedPictureTypes += mediatype + ":" + picturesize + ","
      }
      media.pictureTypes = supportedPictureTypes.substring(0, supportedPictureTypes.length() - 1)
      if (media.save(flush: true, failOnError: true)) {
        obj.addMedia(media)
        respond new SuccessResponse("Media Uploaded successfully", [])
        return
      }
    } catch (Exception ex) {
      println "exception : " + ex
      respond new MediaErrorResponse("Problem in media Upload", [])
      return
    }
  }
}

