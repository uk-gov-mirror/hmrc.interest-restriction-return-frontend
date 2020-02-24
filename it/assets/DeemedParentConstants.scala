package assets

import models.returnModels.DeemedParentModel

object DeemedParentITConstants extends BaseITConstants {

  val deemedParentModelUkCompany = DeemedParentModel(
    companyName = parentCompanyName,
    knownAs = Some(knownAs),
    payTaxInUk = Some(true),
    limitedLiabilityPartnership = Some(false),
    ctutr = Some(ctutrModel),
    registeredCompaniesHouse = Some(true),
    crn = Some(crnModel),
    registeredForTaxInAnotherCountry = Some(false)
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
