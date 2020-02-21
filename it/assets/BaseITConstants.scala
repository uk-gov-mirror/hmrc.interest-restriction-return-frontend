package assets

import models.returnModels.{CRNModel, CompanyNameModel, CountryCodeModel, UTRModel}

trait BaseITConstants {

  val crn = CRNModel("AA111111")
  val utr = UTRModel("1123456789")
  val companyName = CompanyNameModel("A Company Name Ltd")
  val knownAs = "AKA"
  val parentCompanyName = CompanyNameModel("Parent Company Ltd")
  val reportingCompanyName = CompanyNameModel("Reporting Company Ltd")
  val countryOfIncorporation = CountryCodeModel("AF", "Afghanistan")
}
