/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models.returnModels

import config.FrontendAppConfig
import models.{ErrorModel, UserAnswers}
import pages.groupStructure.{CountryOfIncorporationPage, LimitedLiabilityPartnershipPage, ParentCompanyCTUTRPage, ParentCompanyNamePage, ParentCompanySAUTRPage, PayTaxInUkPage, ReportingCompanySameAsParentPage}
import play.api.libs.json.Json

case class DeemedParentModel(companyName: CompanyNameModel,
                             ctutr: Option[UTRModel] = None,
                             sautr: Option[UTRModel] = None,
                             countryOfIncorporation: Option[CountryCodeModel] = None,
                             limitedLiabilityPartnership: Option[Boolean] = None,
                             payTaxInUk: Option[Boolean] = None,
                             reportingCompanySameAsParent: Option[Boolean] = None
                            ) {
  val utr: Option[UTRModel] = ctutr.fold(sautr){ utr => Some(utr)}
}

object DeemedParentModel {

  implicit val format = Json.format[DeemedParentModel]

  def apply(userAnswers: UserAnswers)(implicit appConfig: FrontendAppConfig): Either[ErrorModel, DeemedParentModel] = {

    val oName: Option[String] = userAnswers.get(ParentCompanyNamePage)
    val ctutr: Option[String] = userAnswers.get(ParentCompanyCTUTRPage)
    val sautr: Option[String] = userAnswers.get(ParentCompanySAUTRPage)
    val countryOfIncorporation = userAnswers.get(CountryOfIncorporationPage)
    val limitedLiabilityPartnership: Option[Boolean] = userAnswers.get(LimitedLiabilityPartnershipPage)
    val payTaxInUk: Option[Boolean] = userAnswers.get(PayTaxInUkPage)
    val reportingCompanySameAsParent: Option[Boolean] = userAnswers.get(ReportingCompanySameAsParentPage)

    oName match {
      case Some(name) =>
        Right(DeemedParentModel(
          companyName = CompanyNameModel(name),
          ctutr = ctutr.map(UTRModel.apply),
          sautr = sautr.map(UTRModel.apply),
          countryOfIncorporation = countryOfIncorporation.map{
            countryOfIncorporation => CountryCodeModel(appConfig.countryCodeMap.map(_.swap).apply(countryOfIncorporation), countryOfIncorporation)
          },
          limitedLiabilityPartnership = limitedLiabilityPartnership,
          payTaxInUk = payTaxInUk,
          reportingCompanySameAsParent = reportingCompanySameAsParent
        ))
      case _ => Left(ErrorModel("Cannot Construct Deemed Parent Model from User Answers as Company Name is missing"))
    }
  }
}
