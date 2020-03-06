package assets

import models.returnModels.DeemedParentModel

object DeemedParentITConstants extends BaseITConstants {

  val deemedParentModelUkCompany = DeemedParentModel(
    companyName = parentCompanyName,
    payTaxInUk = Some(true),
    limitedLiabilityPartnership = Some(false),
    ctutr = Some(ctutrModel)
  )

  val deemedParentModelNonUkCompany = DeemedParentModel(
    companyName = parentCompanyName,
    payTaxInUk = Some(false),
    countryOfIncorporation = Some(countryOfIncorporation)
  )
}
