package assets

import models.returnModels.DeemedParentModel

object DeemedParentITConstants extends BaseITConstants {

  val deemedParentModelUkCompany = DeemedParentModel(
    companyName = parentCompanyName,
    knownAs = Some(knownAs),
    payTaxInUk = Some(true),
    limitedLiabilityPartnership = Some(false),
    ctutr = Some(utr),
    registeredCompaniesHouse = Some(true),
    crn = Some(crn),
    registeredForTaxInAnotherCountry = Some(false)
  )

  val deemedParentModelUkPartnership = DeemedParentModel(
    companyName = parentCompanyName,
    knownAs = Some(knownAs),
    payTaxInUk = Some(true),
    limitedLiabilityPartnership = Some(true),
    sautr = Some(utr),
    crn = Some(crn)
  )

  val deemedParentModelNonUkCompany = DeemedParentModel(
    companyName = parentCompanyName,
    knownAs = Some(knownAs),
    payTaxInUk = Some(false),
    countryOfIncorporation = Some(countryOfIncorporation),
    nonUkCrn = Some("nonUkCrn"),
    registeredForTaxInAnotherCountry = Some(true)
  )
}
