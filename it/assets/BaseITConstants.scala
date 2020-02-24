package assets

import models.returnModels.{CRNModel, CompanyNameModel, CountryCodeModel, UTRModel}

trait BaseITConstants {

  val crn = "AA111111"
  val crnModel = CRNModel(crn)
  val knownAs = "AKA"
  val parentCompanyName = CompanyNameModel("Parent Company Ltd")
  val reportingCompanyName = CompanyNameModel("Reporting Company Ltd")
  val countryOfIncorporation = CountryCodeModel("AF", "Afghanistan")
  val ctutr = "1123456789"
  val ctutrModel = UTRModel(ctutr)
  val sautr = "5555555555"
  val sautrModel = UTRModel(sautr)
  val companyName = "A Company Name Ltd"
  val companyNameModel = CompanyNameModel(companyName)
}
