#!/bin/bash

echo ""
echo "Applying migration CompanyAccountingPeriodEndDate"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### CompanyAccountingPeriodEndDate Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "CompanyAccountingPeriodEndDate" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.CompanyAccountingPeriodEndDateController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.CompanyAccountingPeriodEndDateController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.CompanyAccountingPeriodEndDateController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.CompanyAccountingPeriodEndDateController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# CompanyAccountingPeriodEndDatePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "companyAccountingPeriodEndDate.title = CompanyAccountingPeriodEndDate" >> ../conf/messages.en
echo "companyAccountingPeriodEndDate.heading = CompanyAccountingPeriodEndDate" >> ../conf/messages.en
echo "companyAccountingPeriodEndDate.checkYourAnswersLabel = CompanyAccountingPeriodEndDate" >> ../conf/messages.en
echo "companyAccountingPeriodEndDate.error.required.all = Please enter the date for companyAccountingPeriodEndDate" >> ../conf/messages.en
echo "companyAccountingPeriodEndDate.error.required.two= The companyAccountingPeriodEndDate" must include {0} and {1} >> ../conf/messages.en
echo "companyAccountingPeriodEndDate.error.required = The companyAccountingPeriodEndDate must include {0}" >> ../conf/messages.en
echo "companyAccountingPeriodEndDate.error.invalid = Enter a real date" >> ../conf/messages.en

echo ""
echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# CompanyAccountingPeriodEndDatePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "companyAccountingPeriodEndDate.title = CompanyAccountingPeriodEndDate" >> ../conf/messages.cy
echo "companyAccountingPeriodEndDate.heading = CompanyAccountingPeriodEndDate" >> ../conf/messages.cy
echo "companyAccountingPeriodEndDate.checkYourAnswersLabel = CompanyAccountingPeriodEndDate" >> ../conf/messages.cy
echo "companyAccountingPeriodEndDate.error.required.all = Enter the companyAccountingPeriodEndDate" >> ../conf/messages.cy
echo "companyAccountingPeriodEndDate.error.required.two= The companyAccountingPeriodEndDate" must include {0} and {1} >> ../conf/messages.cy
echo "companyAccountingPeriodEndDate.error.required = The companyAccountingPeriodEndDate must include {0}" >> ../conf/messages.cy
echo "companyAccountingPeriodEndDate.error.invalid = Enter a real CompanyAccountingPeriodEndDate" >> ../conf/messages.cy

echo ""
echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyAccountingPeriodEndDateUserAnswersEntry: Arbitrary[(CompanyAccountingPeriodEndDatePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CompanyAccountingPeriodEndDatePage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo ""
echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyAccountingPeriodEndDatePage: Arbitrary[CompanyAccountingPeriodEndDatePage.type] =";\
    print "    Arbitrary(CompanyAccountingPeriodEndDatePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo ""
echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CompanyAccountingPeriodEndDatePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo ""
echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "  def companyAccountingPeriodEndDate: Option[SummaryListRow] = answer(CompanyAccountingPeriodEndDatePage, ukCompaniesRoutes.CompanyAccountingPeriodEndDateController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    CompanyAccountingPeriodEndDatePage.toString -> CompanyAccountingPeriodEndDatePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    CompanyAccountingPeriodEndDatePage.toString -> CompanyAccountingPeriodEndDatePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val companyAccountingPeriodEndDate = \"What is the end date for the first accounting period?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/CompanyAccountingPeriodEndDateControllerISpec.scala

echo "Migration CompanyAccountingPeriodEndDate completed"
