<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Appointment requested</title>
</head>

<body>
<div style="width: 100% !important;-webkit-text-size-adjust: 100%;-ms-text-size-adjust: 100%;margin: 0;padding: 0">
    <!-- Begin logo in header -->
    <div style="display:block">
        <!-- start of header -->
        <table width="100%" bgcolor="#ecf0f1" cellpadding="0" cellspacing="0" border="0"
               st-sortable="header"
               style="margin: 0;padding: 0;width: 100% !important;line-height: 100% !important;border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0;">
            <tbody>
            <tr>
                <td style="border-collapse: collapse">
                    <table width="580" bgcolor="" cellpadding="0" cellspacing="0" border="0" align="center"
                           hlitebg="edit" shadow="edit"
                           style="border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0">
                        <tbody>
                        <tr>
                            <td style="border-collapse: collapse">
                                <!-- logo -->
                                <table width="280" cellpadding="0" cellspacing="0" border="0" align="left"

                                       style="border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0">
                                    <tbody>
                                    <tr>
                                        <td valign="middle" width="270"
                                            style="padding: 5px 0 5px 20px;border-collapse: collapse">
                                            <div>
                                                <a class="logoLink" href="velkare.com"><h1 style="font-family:custom;color:#16a085;padding-top:15px" class="text-center">Velkare</h1></a>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <!-- End of logo --></td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
        <!-- end of header --></div>
    <!-- End logo in header -->

    <!-- Begin Appointment card -->
    <div style="display:block">
        <!-- Full + text -->
        <table width="100%" bgcolor="#ecf0f1" cellpadding="0" cellspacing="0" border="0"
               st-sortable="fullimage"
               style="margin: 0;padding: 0;width: 100% !important;line-height: 100% !important;border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0">
            <tbody>
            <tr>
                <td style="border-collapse: collapse">
                    <table bgcolor="#ffffff" width="580" align="center" cellspacing="0" cellpadding="0" border="0"
                           modulebg="edit"
                           style="border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0">
                        <tbody>
                        <tr>
                            <td width="100%" height="20" style="border-collapse: collapse"/>
                        </tr>
                        <tr>
                            <td style="border-collapse: collapse">
                                <table width="540" align="center" cellspacing="0" cellpadding="0" border="0"
                                       style="border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0
                                       font-family: Arial, sans-serif;
                                        font-size: 14px;
                                        font-weight: 400;
                                        text-rendering: optimizeLegibility !important;
                                        -webkit-font-smoothing: antialiased !important;
                                        -moz-osx-font-smoothing: grayscale !important;
                                        color: #616f77;text-align: left;line-height: 20px;">
                                    <tbody>

                                    <!-- Spacing -->
                                    <tr>
                                        <td width="100%" height="10" style="border-collapse: collapse"/>
                                    </tr>
                                    <!-- Spacing -->
                                    <tr>
                                        <td>
                                            Following are the details of the appointment you've requested. You will receive a confirmation email once your appointment is confirmed with the ${appointmentType}
                                        </td>

                                    </tr>
                                    <!-- end of content --><!-- Spacing -->
                                    <tr>
                                        <td width="100%" height="10" style="border-collapse: collapse"/>
                                    </tr>
                                    <!-- Spacing -->
                                    <g:if test="${appointmentType=='doctor'}">
                                        <g:set var="doctor" value="${appointment.doctor}" />
                                    <tr>
                                        <td style="border-collapse: collapse">

                                            <div class="container w-xxl p-15 bg-white mt-20">
                                                <table width="540" align="center"cellspacing="0" cellpadding="0" border="0"
                                                       style="border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0;line-height: 1.42857143">
                                                    <tbody>
                                                    <tr>
                                                        <td>
                                                            <div>
                                                                <div>
                                                                    <img src="${imageLink}" alt="Doctor photo">
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <ul style="display: inline-block;list-style-type: none;margin: 0;padding: 0;font-size:14px">
                                                                <li>
                                                                    <h2 style="margin:0px; color: #22beef;font-family: Dosis, Arial, sans-serif;font-weight: 500;
                                                                    line-height: 1.1;">${doctor.name}</h2>
                                                                    <strong>
                                                                        <g:if test="${doctor.degree1}">
                                                                        <span>${doctor.degree1}</span>
                                                                        </g:if>
                                                                    <g:if test="${doctor.degree2}">
                                                                        , <span>${doctor.degree1}</span>
                                                                    </g:if>
                                                                    <g:if test="${doctor.degree3}">
                                                                        , <span>${doctor.degree3}</span>
                                                                    </g:if>
                                                                    <g:if test="${doctor.degree4}">
                                                                        , <span>${doctor.degree4}</span>
                                                                    </g:if>

                                                                </strong>
                                                                </li>
                                                                <g:if test="${doctor.specilalitiesNamesList}">
                                                                <li>
                                                                    <span style="color: #95a2a9;">${doctor.specilalitiesNamesList.join(', ')}</span>
                                                                </li>
                                                                </g:if>
                                                                <g:if test="${appointment.hospital}" >
                                                                    <g:set var="hospital" value="${appointment.hospital}" />
                                                                <li>
                                                                    <address style="font-style: normal;margin-top: 10px;
                                                                             line-height: 1.1;">
                                                                        <strong>${hospital.name.toLowerCase().capitalize()}</strong><br>
                                                                        <g:if test="${hospital.address1}">
                                                                         <span>${hospital.address1}</span>
                                                                        </g:if>
                                                                        <g:if test="${hospital.address2}">
                                                                            , <span>${hospital.address2}</span>
                                                                        </g:if>
                                                                        <g:if test="${hospital.address3}">
                                                                            , <span>${hospital.address3}</span>
                                                                        </g:if>
                                                                        <g:if test="${hospital.address4}">
                                                                            , <span>${hospital.address4}</span>
                                                                        </g:if><br/>
                                                                        <g:if test="${hospital.city}">
                                                                            , <span>${hospital.city}</span>
                                                                        </g:if>
                                                                        <g:if test="${hospital.district}">
                                                                            , <span>${hospital.district}</span>
                                                                        </g:if>
                                                                        <g:if test="${hospital.state}">
                                                                            , <span>${hospital.state}</span>
                                                                        </g:if>
                                                                        <g:if test="${hospital.country}">
                                                                            , <span>${hospital.country.toLoweCase().capitalize()}</span>
                                                                        </g:if>
                                                                        <g:if test="${hospital.postalCode}">
                                                                           ,  <span>${hospital.postalCode}</span>
                                                                        </g:if>


                                                                    </address>
                                                                </li>
                                                                </g:if>
                                                                <li style="margin-top: 15px;">
                                                                    On: ${com.vellkare.util.DateUtil.getDateStringUserFormat(appointment.fromTime)}
                                                                </li>
                                                                <li>
                                                                    ${com.vellkare.util.DateUtil.getPrintableTimeString(appointment.fromTime)} to ${com.vellkare.util.DateUtil.getPrintableTimeString(appointment.toTime)}
                                                                </li>
                                                            </ul>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    </g:if>
                                    <g:if test="${appointmentType=='lab'}">
                                        <g:set var="lab" value="${appointment.lab}" />
                                    <tr>
                                        <td style="border-collapse: collapse">

                                            <div class="container w-xxl p-15 bg-white mt-20">
                                                <table width="540" align="center"cellspacing="0" cellpadding="0" border="0"
                                                       style="border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0;line-height: 1.42857143">
                                                    <tbody>
                                                    <tr>
                                                        <td>
                                                            <div>
                                                                <div>
                                                                    <img src="${imageLink}" alt="Lab photo">
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <ul style="display: inline-block;list-style-type: none;margin: 0;padding: 0;font-size:14px">
                                                                <li>
                                                                    <h2 style="margin:0px; color: #22beef;font-family: Dosis, Arial, sans-serif;font-weight: 500;
                                                                    line-height: 1.1;">${lab.name.toLowerCase().capitalize()}</h2>
                                                                </li>

                                                                <li>
                                                                    <address style="font-style: normal; margin-top: 10px;
                                                                             line-height: 1.4;">
                                                                        <g:if test="${lab.address1}">
                                                                            <span>${lab.address1}</span>
                                                                        </g:if>
                                                                        <g:if test="${lab.address2}">
                                                                            , <span>${lab.address2}</span>
                                                                        </g:if>
                                                                        <g:if test="${lab.address3}">
                                                                            , <span>${lab.address3}</span>
                                                                        </g:if>
                                                                        <g:if test="${lab.address4}">
                                                                            , <span>${lab.address4}</span>
                                                                        </g:if><br/>
                                                                        <g:if test="${lab.city}">
                                                                            , <span>${lab.city}</span>
                                                                        </g:if>
                                                                        <g:if test="${lab.district}">
                                                                            , <span>${lab.district}</span>
                                                                        </g:if>
                                                                        <g:if test="${lab.state}">
                                                                            , <span>${lab.state}</span>
                                                                        </g:if>
                                                                        <g:if test="${lab.country}">
                                                                            , <span>${lab.country.toLoweCase().capitalize()}</span>
                                                                        </g:if>
                                                                        <g:if test="${lab.postalCode}">
                                                                            , <span>${lab.postalCode}</span>
                                                                        </g:if>
                                                                    </address>
                                                                </li>
                                                                <li style="margin-top: 15px;">
                                                                    On: ${com.vellkare.util.DateUtil.getDateStringUserFormat(appointment.fromTime)}
                                                                </li>
                                                                <li>
                                                                    ${com.vellkare.util.DateUtil.getPrintableTimeString(appointment.fromTime)} to ${com.vellkare.util.DateUtil.getPrintableTimeString(appointment.toTime)}
                                                                </li>
                                                            </ul>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    </g:if>


                                    <!-- Spacing -->
                                    <tr>
                                        <td width="100%" height="10" style="border-collapse: collapse"/>
                                    </tr>

                                    <!-- Spacing -->
                                    <tr>
                                        <td width="100%" height="10" style="border-collapse: collapse"/>
                                    </tr>
                                    <!-- Spacing -->
                                    <tr>
                                        <td>
                                            For any queries about your appointment,
                                            call <span style="color: #22beef">9988990000</span> or email
                                            <a href="mailto:support@velkare.com?Subject=Questions about appointment ${appointment.id}" target="_top">support@velkare.com</a>
                                        </td>

                                    </tr>
                                    <!-- end of content --><!-- Spacing -->
                                    <tr>
                                        <td width="100%" height="10" style="border-collapse: collapse"/>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <!-- End Appointment card -->

    <!-- Begin Footer with legal links -->
    <div style="display:block">
        <!-- Start of preheader -->
        <table width="100%" bgcolor="#ecf0f1" cellpadding="0" cellspacing="0" border="0" id="backgroundTable"
               st-sortable="postfooter"
               style="margin: 0;padding: 0;width: 100% !important;line-height: 100% !important;border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0">
            <tbody>
            <tr>
                <td width="100%" style="border-collapse: collapse">
                    <table width="580" cellpadding="0" cellspacing="0" border="0" align="center"
                           style="border-collapse: collapse;mso-table-lspace: 0;mso-table-rspace: 0">
                        <tbody><!-- Spacing -->
                        <tr>
                            <td width="100%" height="5" style="border-collapse: collapse"/>
                        </tr>
                        <!-- Spacing -->
                        <tr>
                            <td align="right" valign="middle"
                                style="font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif;font-size: 10px;color: #2c3e50;border-collapse: collapse"
                                st-content="preheader">
                                <a style="padding-right: 20px" href="/legal/privacyPolicy.html">Privacy Policy</a><!--  ADD LEGAL LINKS HERE -->
                                <a href="/legal/termsOfUse.html">Terms of Use</a>
                            </td>
                        </tr>
                        <!-- Spacing -->
                        <tr>
                            <td width="100%" height="5" style="border-collapse: collapse"/>
                        </tr>
                        <!-- Spacing --></tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
        <!-- End of preheader -->
    </div>
    <!-- End Footer with legal links -->
</div>
<style>
    a.logoLink
    {
        text-decoration:none !important;
    }
</style>
</body>
</html>
