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

package pages.aboutReturn

import models.SectionStatus.writes
import models.{FullOrAbbreviatedReturn, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.elections.AddInvestorGroupPage
import pages.groupLevelInformation.{GroupInterestAllowancePage, RevisingReturnPage}
import pages.ukCompanies.ConsentingCompanyPage
import pages.ultimateParentCompany.ParentCompanySAUTRPage

class FullOrAbbreviatedReturnPageSpec extends PageBehaviours {

  "FullOrAbbreviatedReturnPage" must {

    beRetrievable[FullOrAbbreviatedReturn](FullOrAbbreviatedReturnPage)

    beSettable[FullOrAbbreviatedReturn](FullOrAbbreviatedReturnPage)

    beRemovable[FullOrAbbreviatedReturn](FullOrAbbreviatedReturnPage)
  }

  "Cleanup" when {
    "A user has selected `Full` return" when {
      "They revisit the page but do not change the answer" should {
        "Not delete any data" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.head).success.value
                .set(RevisingReturnPage,false).success.value
                .set(AgentNamePage,"John Doe").success.value
                .set(ReportingCompanyAppointedPage,true).success.value
                .set(GroupInterestAllowancePage,BigDecimal(3.324234234)).success.value
                .set(ConsentingCompanyPage,true).success.value
                .set(AddInvestorGroupPage,true).success.value
                .set(ParentCompanySAUTRPage,"111111111").success.value
                .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.head).success.value


              result.get(ReportingCompanyAppointedPage) mustBe defined
              result.get(RevisingReturnPage) mustBe defined
              result.get(AgentNamePage) mustBe defined
              result.get(GroupInterestAllowancePage) mustBe defined
              result.get(ConsentingCompanyPage) mustBe defined
              result.get(AddInvestorGroupPage) mustBe defined
              result.get(ParentCompanySAUTRPage) mustBe defined
        }
      }
    }

    "They revisit the page and change the answer to `Abbreviated`" should {
      "remove all answers from 'FullOrAbbreviatedReturnPage' onwards" in {
        forAll(arbitrary[UserAnswers]) {
          userAnswers =>
            val result = userAnswers
              .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.last).success.value
              .set(ReportingCompanyAppointedPage,true).success.value
              .set(AgentActingOnBehalfOfCompanyPage, true).success.value
              .set(AgentNamePage,"John Doe").success.value
              .set(RevisingReturnPage,false).success.value
              .set(GroupInterestAllowancePage,BigDecimal(3.324234234)).success.value
              .set(ConsentingCompanyPage,true).success.value
              .set(AddInvestorGroupPage,true).success.value
              .set(ParentCompanySAUTRPage,"111111111").success.value
              .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.head).success.value


            result.get(ReportingCompanyAppointedPage) mustBe defined
            result.get(AgentNamePage) mustBe defined
            result.get(AgentActingOnBehalfOfCompanyPage) mustBe defined
            result.get(FullOrAbbreviatedReturnPage) mustBe defined
            result.get(RevisingReturnPage) must not be defined
            result.get(GroupInterestAllowancePage) must not be defined
            result.get(ConsentingCompanyPage) must not be defined
            result.get(AddInvestorGroupPage) must not be defined
            result.get(ParentCompanySAUTRPage) must not be defined
        }
      }
    }
    }
    "A user has selected `Abbreviated` return" when {
      "When they revisit the page but do not change the answer" should {
        "Not delete any data" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.last).success.value
                .set(RevisingReturnPage,false).success.value
                .set(AgentNamePage,"John Doe").success.value
                .set(ReportingCompanyAppointedPage,true).success.value
                .set(GroupInterestAllowancePage,BigDecimal(3.324234234)).success.value
                .set(ConsentingCompanyPage,true).success.value
                .set(AddInvestorGroupPage,true).success.value
                .set(ParentCompanySAUTRPage,"111111111").success.value
                .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.last).success.value


              result.get(ReportingCompanyAppointedPage) mustBe defined
              result.get(RevisingReturnPage) mustBe defined
              result.get(AgentNamePage) mustBe defined
              result.get(GroupInterestAllowancePage) mustBe defined
              result.get(ConsentingCompanyPage) mustBe defined
              result.get(AddInvestorGroupPage) mustBe defined
              result.get(ParentCompanySAUTRPage) mustBe defined
          }
        }
      }

      "When they revisit the page and change the answer to `Full`" should {
        "remove all answers from 'FullOrAbbreviatedReturnPage' onwards" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.head).success.value
                .set(ReportingCompanyAppointedPage,true).success.value
                .set(AgentActingOnBehalfOfCompanyPage, true).success.value
                .set(AgentNamePage,"John Doe").success.value
                .set(RevisingReturnPage,false).success.value
                .set(GroupInterestAllowancePage,BigDecimal(3.324234234)).success.value
                .set(ConsentingCompanyPage,true).success.value
                .set(AddInvestorGroupPage,true).success.value
                .set(ParentCompanySAUTRPage,"111111111").success.value
                .set(FullOrAbbreviatedReturnPage,FullOrAbbreviatedReturn.values.last).success.value


              result.get(ReportingCompanyAppointedPage) mustBe defined
              result.get(AgentNamePage) mustBe defined
              result.get(AgentActingOnBehalfOfCompanyPage) mustBe defined
              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(RevisingReturnPage) must not be defined
              result.get(GroupInterestAllowancePage) must not be defined
              result.get(ConsentingCompanyPage) must not be defined
              result.get(AddInvestorGroupPage) must not be defined
              result.get(ParentCompanySAUTRPage) must not be defined
          }
        }
      }
    }
  }
}
