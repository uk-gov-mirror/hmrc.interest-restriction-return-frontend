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

package controllers.ukCompanies

import java.time.{Instant, ZoneOffset, LocalDate}
import assets.{BaseITConstants, PageTitles}
import play.api.http.Status._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import assets.UkCompanyITConstants.ukCompanyModelMax
import pages.ukCompanies.UkCompaniesPage
import models.returnModels.AccountingPeriodModel
import pages.aboutReturn.AccountingPeriodPage
import play.api.libs.json.Json
import models.NormalMode

class CompanyAccountingPeriodEndDateControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val idx = 1

  val periodOfAccount = AccountingPeriodModel(LocalDate.of(2017,1,1), LocalDate.of(2018,1,1))
  val userAnswers = (for {
    ua  <- emptyUserAnswers.set(AccountingPeriodPage, periodOfAccount)
    ua2 <- ua.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
  } yield ua2).get

  "in Normal mode" when {

    "GET /ukCompanies/1/restrictions/1/company-accounting-period-end-date" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(userAnswers)
          val res = getRequest("/uk-companies/1/restrictions/1/company-accounting-period-end-date")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.companyAccountingPeriodEndDate)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()
          setAnswers(userAnswers)

          val res = getRequest("/uk-companies/1/restrictions/1/company-accounting-period-end-date")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ukCompanies/2/restrictions/1/company-accounting-period-end-date" when {

      "a previous company does not have a restriction set" should {

        "return OK (200)" in {

          val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(AccountingPeriodPage, periodOfAccount)
            ua2 <- ua.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
            ua3 <- ua2.set(UkCompaniesPage, ukCompanyModelMax, Some(2))
          } yield ua3).get

          AuthStub.authorised()
          setAnswers(userAnswers)

          val res = postRequest("/uk-companies/2/restrictions/1/company-accounting-period-end-date", 
            Json.obj("value.day" -> now.getDayOfMonth,
                    "value.month" -> now.getMonthValue,
                    "value.year" -> now.getYear))()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.ukCompanies.routes.AddRestrictionAmountController.onPageLoad(2, 1, NormalMode).url)
            )
          }
        }
      }
    }
  }

  "in Change mode" when {

    "GET /uk-companies/1/restrictions/1/company-accounting-period-end-date" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(userAnswers)

          val res = getRequest("/uk-companies/1/restrictions/1/company-accounting-period-end-date/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.companyAccountingPeriodEndDate)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()
          setAnswers(userAnswers)

          val res = getRequest("/uk-companies/1/restrictions/1/company-accounting-period-end-date/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }
  }
}
