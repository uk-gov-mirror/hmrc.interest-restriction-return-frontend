#!/bin/bash

echo ""
echo "Applying migration CompanyAccountingPeriodSameAsGroup"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### CompanyAccountingPeriodSameAsGroup Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "CompanyAccountingPeriodSameAsGroup" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.CompanyAccountingPeriodSameAsGroupController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.CompanyAccountingPeriodSameAsGroupController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.CompanyAccountingPeriodSameAsGroupController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.CompanyAccountingPeriodSameAsGroupController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# CompanyAccountingPeriodSameAsGroupPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "companyAccountingPeriodSameAsGroup.title = Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.en
echo "companyAccountingPeriodSameAsGroup.heading = Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.en
echo "companyAccountingPeriodSameAsGroup.checkYourAnswersLabel = Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.en
echo "companyAccountingPeriodSameAsGroup.error.required = Select yes if Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# CompanyAccountingPeriodSameAsGroupPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "companyAccountingPeriodSameAsGroup.title = Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.cy
echo "companyAccountingPeriodSameAsGroup.heading = Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.cy
echo "companyAccountingPeriodSameAsGroup.checkYourAnswersLabel = Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.cy
echo "companyAccountingPeriodSameAsGroup.error.required = Select yes if Is this company's accounting period the same as the group's period of account?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyAccountingPeriodSameAsGroupUserAnswersEntry: Arbitrary[(CompanyAccountingPeriodSameAsGroupPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CompanyAccountingPeriodSameAsGroupPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyAccountingPeriodSameAsGroupPage: Arbitrary[CompanyAccountingPeriodSameAsGroupPage.type] =";\
    print "    Arbitrary(CompanyAccountingPeriodSameAsGroupPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CompanyAccountingPeriodSameAsGroupPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def companyAccountingPeriodSameAsGroup: Option[SummaryListRow] = answer(CompanyAccountingPeriodSameAsGroupPage, ukCompaniesRoutes.CompanyAccountingPeriodSameAsGroupController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    CompanyAccountingPeriodSameAsGroupPage.toString -> CompanyAccountingPeriodSameAsGroupPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    CompanyAccountingPeriodSameAsGroupPage.toString -> CompanyAccountingPeriodSameAsGroupPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val companyAccountingPeriodSameAsGroup = \"Is this company's accounting period the same as the group's period of account?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/CompanyAccountingPeriodSameAsGroupControllerISpec.scala

echo "Migration CompanyAccountingPeriodSameAsGroup completed"
