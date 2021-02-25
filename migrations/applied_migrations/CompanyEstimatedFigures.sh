#!/bin/bash

echo ""
echo "Applying migration CompanyEstimatedFigures"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### CompanyEstimatedFigures Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "CompanyEstimatedFigures" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.CompanyEstimatedFiguresController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.CompanyEstimatedFiguresController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.CompanyEstimatedFiguresController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.CompanyEstimatedFiguresController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# CompanyEstimatedFiguresPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "companyEstimatedFigures.title = Tell us which figures have been estimated" >> ../conf/messages.en
echo "companyEstimatedFigures.heading = Tell us which figures have been estimated" >> ../conf/messages.en
echo "companyEstimatedFigures.tax EBITDA = Net tax interest amount" >> ../conf/messages.en
echo "companyEstimatedFigures.netTaxInterest = Net tax interest amount" >> ../conf/messages.en
echo "companyEstimatedFigures.checkYourAnswersLabel = Tell us which figures have been estimated" >> ../conf/messages.en
echo "companyEstimatedFigures.error.required = Select companyEstimatedFigures" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# CompanyEstimatedFiguresPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "companyEstimatedFigures.title = Tell us which figures have been estimated" >> ../conf/messages.cy
echo "companyEstimatedFigures.heading = Tell us which figures have been estimated" >> ../conf/messages.cy
echo "companyEstimatedFigures.tax EBITDA = Net tax interest amount" >> ../conf/messages.cy
echo "companyEstimatedFigures.netTaxInterest = Net tax interest amount" >> ../conf/messages.cy
echo "companyEstimatedFigures.checkYourAnswersLabel = Tell us which figures have been estimated" >> ../conf/messages.cy
echo "companyEstimatedFigures.error.required = Select companyEstimatedFigures" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyEstimatedFiguresUserAnswersEntry: Arbitrary[(CompanyEstimatedFiguresPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[CompanyEstimatedFiguresPage.type]";\
    print "        value <- arbitrary[CompanyEstimatedFigures].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyEstimatedFiguresPage: Arbitrary[CompanyEstimatedFiguresPage.type] =";\
    print "    Arbitrary(CompanyEstimatedFiguresPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryCompanyEstimatedFigures: Arbitrary[CompanyEstimatedFigures] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(CompanyEstimatedFigures.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(CompanyEstimatedFiguresPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def companyEstimatedFigures: Option[SummaryListRow] = answer(CompanyEstimatedFiguresPage, ukCompaniesRoutes.CompanyEstimatedFiguresController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    CompanyEstimatedFiguresPage.toString -> CompanyEstimatedFiguresPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    CompanyEstimatedFiguresPage.toString -> CompanyEstimatedFiguresPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val companyEstimatedFigures = \"Tell us which figures have been estimated\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/CompanyEstimatedFiguresControllerISpec.scala

echo "Migration CompanyEstimatedFigures completed"
