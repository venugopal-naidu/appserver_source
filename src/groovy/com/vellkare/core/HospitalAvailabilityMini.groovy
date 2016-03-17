package com.vellkare.core

/**
 * Created by roopesh on 15/03/16.
 */
class HospitalAvailabilityMini {
  Long id
  String name;
  String address1
  String address2
  String address3
  String address4
  Map<String, List<AvailabilityMini> > availability = new LinkedHashMap<>()
}
