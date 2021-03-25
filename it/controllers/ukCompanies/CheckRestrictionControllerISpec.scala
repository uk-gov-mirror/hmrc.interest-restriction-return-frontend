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

import assets.{BaseITConstants, PageTitles}
import assets.UkCompanyITConstants._
import pages.ukCompanies.{AddRestrictionAmountPage, CompanyAccountingPeriodEndDatePage, RestrictionAmountForAccountingPeriodPage, UkCompaniesPage}
import play.api.http.Status._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import views.ViewUtils.addPossessive

import java.time.{LocalDate, ZoneOffset}

class CheckRestrictionControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/1/restrictions/1/check" when {

      "user is authorised" should {

        "return OK (200)" in {

          lazy val userAnswers = (for {
            comp <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, idx = Some(1))
            cap <- comp.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.now(ZoneOffset.UTC))
            ara <- cap.set(AddRestrictionAmountPage(1, 1), true)
            ua <- ara.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(1234.56))
          } yield ua).success.value

          setAnswers(userAnswers)

          AuthStub.authorised()
          val res = getRequest("/uk-companies/1/restrictions/1/check")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.checkRestriction)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/1/restrictions/1/check")()

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
