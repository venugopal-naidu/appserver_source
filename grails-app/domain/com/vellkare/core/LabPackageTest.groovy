package com.vellkare.core

import org.apache.commons.lang.builder.HashCodeBuilder

class LabPackageTest implements Serializable {
  Lab lab;
  Test test
  Long _package
  Double labPackageCost = 0
  Double velkarePackageCost = 0

  static belongsTo = [ lab: Lab, test: Test]

  static constraints = {
  }

  static mapping = {
    id composite: [ 'lab','_package', 'test']
    _package column: 'package_id'
  }

  boolean equals(other) {
    if (!(other instanceof LabPackageTest)) {
      return false
    }

    other.lab?.id == lab?.id &&
      other._package == _package &&
      other.test?.id == test?.id
  }

  int hashCode() {
    def builder = new HashCodeBuilder()
    if (lab) builder.append(lab.id)
    if (_package) builder.append('.').append(_package)
    if (test) builder.append('.').append(test.id)
    builder.toHashCode()
  }
}
