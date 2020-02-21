package assets

import models.returnModels.{CompanyNameModel, UTRModel}

trait BaseITConstants {

  val crn = "AA111111"
  val ctutr = "1123456789"
  val ctutrModel = UTRModel(ctutr)
  val companyName = "A Company Name Ltd"
  val companyNameModel = CompanyNameModel(companyName)
  val parentCompanyName = "Parent Company Ltd"
  val reportingCompanyName = "Reporting Company Ltd"
}
