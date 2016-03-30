package com.vellkare.core

import com.vellkare.api.FieldErrorApiModel
import com.vellkare.api.SuccessResponse
import com.vellkare.api.ValidationErrorResponse
import com.vellkare.util.DateUtil
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.apache.http.HttpStatus
import org.springframework.web.multipart.commons.CommonsMultipartFile

class MediaController extends RestfulController {
  static responseFormats = ['json', 'xml']
  static namespace = 'v0'
  def securityService


  @Transactional
  def listMedicalRecordTypes() {
    respond MedicalRecordType.findAllWhere(enabled: true).collect() {
      [id: it.id, name: it.name]
    }
  }

  @Secured("#oauth2.isUser()")
  @Transactional
  def listMedicalRecords() {
    def member = securityService.currentMember
    def medicalRecords = MedicalRecord.findAllWhere(member: member, deleted: false)
    respond(medicalRecords.collect { record ->
      [id        : record.id, name: record.name, notes: record.notes, recordType: record.recordType.name, appointmentId: record.appointmentId,
       recordDate: DateUtil.getDateStringUserFormat(record.recordDate),
       uploadDate: DateUtil.getDateTimeStringUserFormat(record.uploadDate),
       record    : [name    : record.media?.fileName, contentType: record.media?.contentType, size: record.media?.size,
                    fileLink: "record/download/${record.id}"]
      ]
    })
  }

  @Transactional
  @Secured("#oauth2.isUser()")
  def downloadMedicalRecord(Long id) {
    def member = securityService.currentMember
    MedicalRecord medicalRecord = MedicalRecord.findWhere(id: id, member: member, deleted: false)
    if (!medicalRecord) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('id', 'record.invalid', [])])
      return
    }

    Media media = medicalRecord.media
    response.setContentType(media.contentType ?: "APPLICATION/OCTET-STREAM")
    response.setHeader("Content-Disposition", "Attachment;Filename=\"${media.fileName}\"")
    def outputStream = response.getOutputStream()
    outputStream << media.mediaData.fileData
    outputStream.flush()
    outputStream.close()
    return
  }

  @Secured("#oauth2.isUser()")
  @Transactional
  def deleteMedicalRecord(Long id) {
    def member = securityService.currentMember
    def medicalRecord = MedicalRecord.findWhere(id: id, member: member, deleted: false)
    if (!medicalRecord) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('id', 'record.invalid', [])])
      return
    }
    medicalRecord.deleteRecord()
    medicalRecord.save(flush: true, failOnError: true)
    respond new SuccessResponse("Record deleted successfully", [])
  }

  @Secured("#oauth2.isUser()")
  @Transactional
  def saveMedicalRecord(HealthRecord cmd) {
    def member = securityService.currentMember
    def mediaConfig = grailsApplication.config.vellkare.media.healthRecord
    def mediaPath = mediaConfig.path
    CommonsMultipartFile photoFile = request.getFile('file')
    if (photoFile.size == 0 || photoFile.size > mediaConfig?.maxSize) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('file', 'file.size.exceeded', [])])
      return
    }

    if (!cmd.recordTypeId || !MedicalRecordType.get(cmd.recordTypeId)) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('recordTypeId', 'recordTypeId.invalid', [])])
      return
    }

    if (cmd.appointmentId && (!Appointment.get(cmd.appointmentId)
      || Appointment.get(cmd.appointmentId).member.id != member?.id)) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('appointmentId', 'appointmentId.invalid', [])])
      return
    }

    def extension = photoFile.originalFilename.indexOf('.') > 0 ? photoFile.originalFilename.tokenize(".").last() : null
    if (!extension || !(extension.toLowerCase() in mediaConfig.supportedTypes)) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('file', 'file.type.invalid', [])])
      return
    }

    Media media = new Media(fileName: photoFile.originalFilename, size: photoFile.size,
      contentType: photoFile.contentType)
    media.mediaData = new MediaData(media: media, fileData: photoFile.bytes)

    MedicalRecord record = new MedicalRecord(member: member, notes: cmd.notes, name: cmd.name,
      recordType: MedicalRecordType.get(cmd.recordTypeId),
      appointment: cmd.appointmentId ? Appointment.get(cmd.appointmentId) : null,
      recordDate: cmd.recordDate ? DateUtil.parseDateStringUserFormat(cmd.recordDate) : null)
    record.media = media
    if (record.save(flush: true, failOnError: true)) {
      //respond new SuccessResponse("Record Uploaded successfully", [])
      respond([id        : record.id, notes: record.notes, recordType: record.recordType.name, appointmentId: record.appointmentId,
               recordDate: DateUtil.getPrintableDateTimeString(record.recordDate),
               uploadDate: DateUtil.getPrintableDateTimeString(record.uploadDate),
               record    : [name: record.media?.fileName, contentType: record.media?.contentType, size: record.media?.size]
      ])
      return
    } else {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('file', 'file.save.failed', [])])
      return
    }
  }
}

class HealthRecord {
  Long fileId
  Long recordTypeId
  String notes
  Long appointmentId
  String recordDate
  String name
}


