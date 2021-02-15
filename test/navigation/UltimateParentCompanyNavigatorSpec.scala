/*
 * Copyright 2021 HM Revenue & Customs
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

package navigation

import assets.constants.DeemedParentConstants._
import base.SpecBase
import controllers.elections.routes
import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import models._
import models.returnModels.UTRModel
import pages.ultimateParentCompany._

class UltimateParentCompanyNavigatorSpec extends SpecBase {

  val navigator = new UltimateParentCompanyNavigator

  "ultimateParentCompanyNavigator" when {

    "for each possible 1 value (when handling multiple deemed parents)" must {

      "in Normal mode" must {

        "from the ReportingCompanySameAsParentPage" should {

          "go to the DeemedParentPage when given false" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, false).success.value

            navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers, Some(1)) mustBe
              ultimateParentCompanyRoutes.HasDeemedParentController.onPageLoad(NormalMode)
          }

          "go to the Group Ratio Election page when given true" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, true).success.value

            navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers, Some(1)) mustBe
              controllers.elections.routes.GroupRatioElectionController.onPageLoad(NormalMode)
          }

          "go to the ReportingCompanySameAsParentPage if not answered" in {

            navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
          }
        }

        "from the DeemedParentPage" should {

          "go to the ParentCompanyNamePage when false is selected" in {

            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, false).success.value

            navigator.nextPage(HasDeemedParentPage, NormalMode, userAnswers, Some(1)) mustBe
              ultimateParentCompanyRoutes.ParentCompanyNameController.onPageLoad(1, NormalMode)
          }

          "go to the ParentCompanyNamePage when given true" in {

            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value

            navigator.nextPage(HasDeemedParentPage, NormalMode, userAnswers, Some(1)) mustBe
              ultimateParentCompanyRoutes.ParentCompanyNameController.onPageLoad(1, NormalMode)
          }

          "go to the ParentCompanyPage" in {

            navigator.nextPage(HasDeemedParentPage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.ParentCompanyNameController.onPageLoad(1, NormalMode)
          }
        }

        "from the ParentCompanyNamePage" should {

          "go to the PayTaxInUkPage" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelMin, Some(1)).success.value

            navigator.nextPage(ParentCompanyNamePage, NormalMode, userAnswers) mustBe
              ultimateParentCompanyRoutes.PayTaxInUkController.onPageLoad(1, NormalMode)
          }
        }

        "from the PayTaxInUkPage" should {

          "go to the LimitedLiabilityPartnershipPage when given true" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value

            navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers, Some(1)) mustBe
              ultimateParentCompanyRoutes.LimitedLiabilityPartnershipController.onPageLoad(1, NormalMode)
          }

          "go to the Country of Incorporation Page when given false" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value

            navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers, Some(1)) mustBe
              ultimateParentCompanyRoutes.CountryOfIncorporationController.onPageLoad(1, NormalMode)
          }

          "go to the PayTaxInUkPage if not answered" in {

            navigator.nextPage(PayTaxInUkPage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.PayTaxInUkController.onPageLoad(1, NormalMode)
          }
        }

        "from the LimitedLiabilityPartnershipPage" should {

          "go to the ParentCompanySAUTRPage when given true" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkPartnership, Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers, Some(1)) mustBe
              ultimateParentCompanyRoutes.ParentCompanySAUTRController.onPageLoad(1, NormalMode)
          }

          "go to the ParentCompanyCTUTRPage when given false" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers, Some(1)) mustBe
              ultimateParentCompanyRoutes.ParentCompanyCTUTRController.onPageLoad(1, NormalMode)
          }

          "go to the LimitedLiabilityPartnershipPage when no answer" in {

            navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.LimitedLiabilityPartnershipController.onPageLoad(1, NormalMode)
          }
        }

        "from the ParentCompanyCTUTRPage" should {

          "go to the Check Answers page" in {

            navigator.nextPage(ParentCompanyCTUTRPage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }
        }


        "from the ParentCompanySAUTRPage" should {

          "go to the Check Answers page" in {

            navigator.nextPage(ParentCompanySAUTRPage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }
        }

        "from the Country of Incorporation page" should {

          "go to the Check Answers page" in {

            navigator.nextPage(CountryOfIncorporationPage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }
        }

        "from the check answers page" should {

          "when parent company is deemed" should {

            "go to the DeemedParentReviewAnswersList page" in {

              val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value

              navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, userAnswers) mustBe
                ultimateParentCompanyRoutes.DeemedParentReviewAnswersListController.onPageLoad()
            }
          }

          "when parent company is ultimate" should {

            "go to the group ratio election page when given false" in {

              val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, false).success.value

              navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, userAnswers) mustBe
                controllers.elections.routes.GroupRatioElectionController.onPageLoad(NormalMode)
            }
          }

          "go to the HasDeemedParent page no given answers for HasDeemedParentPage" in {

            navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, emptyUserAnswers) mustBe
              ultimateParentCompanyRoutes.HasDeemedParentController.onPageLoad(NormalMode)
          }
        }
      }

      "in Check mode" must {
        "from Limited Liability Partnership page" when {
          "go down to sautr page if user selects `yes` and we have no `sautr`" in {
            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelMin.copy(limitedLiabilityPartnership = Some(true),sautr = None), Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, CheckMode, userAnswers) mustBe
              ultimateParentCompanyRoutes.ParentCompanySAUTRController.onPageLoad(1,CheckMode)
          }

          "go back to CYA if user selects `yes` and there is already a `sautr`" in {
            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelMin.copy(limitedLiabilityPartnership = Some(true),sautr = Some(UTRModel("test"))), Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, CheckMode, userAnswers) mustBe
              ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }

          "go down to ctutr page if user selects `no` and we have no `ctutr`" in {
            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelMin.copy(limitedLiabilityPartnership = Some(false),ctutr = None), Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, CheckMode, userAnswers) mustBe
              ultimateParentCompanyRoutes.ParentCompanyCTUTRController.onPageLoad(1,CheckMode)
          }

          "go back to CYA if user selects `no` and there is already a `ctutr`" in {
            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelMin.copy(limitedLiabilityPartnership = Some(false),ctutr = Some(UTRModel("test"))), Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, CheckMode, userAnswers) mustBe
              ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }
        }

        "from the Reporting Company Same As Parent page" should {
          "go to the Check Your Answers page when the user says `no`" in {
            val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, false).success.value
              .set(HasDeemedParentPage, true).success.value

            navigator.nextPage(ReportingCompanySameAsParentPage, CheckMode, userAnswers) mustBe
              ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }

          "go down the normal flow if the user says `no` and we have no data" in {
            val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, false).success.value

            navigator.nextPage(ReportingCompanySameAsParentPage, CheckMode, userAnswers) mustBe
              ultimateParentCompanyRoutes.HasDeemedParentController.onPageLoad(NormalMode)
          }

          "go down the normal route if the user says `yes`" in {
            val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, true)
              .map(reportingCompanySame => reportingCompanySame)

            navigator.nextPage(ReportingCompanySameAsParentPage, CheckMode, userAnswers.get) mustBe
              routes.GroupRatioElectionController.onPageLoad(NormalMode)
          }
        }

        "from the Has Deemed Parent page" should {
          "go down the Check Yoour Answer page when there is no changes in the answer" in {
            val userAnswers = for {
              hasDeemedParentPage <- emptyUserAnswers.set(HasDeemedParentPage, false)
              parentCompanyNamePage <- hasDeemedParentPage.set(ParentCompanyNamePage, "company name")
            } yield parentCompanyNamePage
            val idx = 1

            navigator.nextPage(HasDeemedParentPage, CheckMode, userAnswers.get, Some(idx)) mustBe
              ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(idx)
          }

          "go down the normal flow if the user changes in the answer" in {
            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, false)
            val idx = 1

            navigator.nextPage(HasDeemedParentPage, CheckMode, userAnswers.get, Some(idx)) mustBe
              ultimateParentCompanyRoutes.ParentCompanyNameController.onPageLoad(idx, NormalMode)
          }
        }
      }
    }
  }
}
