#!/bin/bash

echo ""
echo "Applying migration EnterCompanyTaxEBITDA"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### EnterCompanyTaxEBITDA Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "EnterCompanyTaxEBITDA" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.EnterCompanyTaxEBITDAController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.EnterCompanyTaxEBITDAController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.EnterCompanyTaxEBITDAController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.EnterCompanyTaxEBITDAController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# EnterCompanyTaxEBITDAPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.title = EnterCompanyTaxEBITDA" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.heading = EnterCompanyTaxEBITDA" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.label= Enter company's Tax-EBITDA" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.checkYourAnswersLabel = EnterCompanyTaxEBITDA" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.error.nonNumeric = Enter your enterCompanyTaxEBITDA using numbers" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.error.required = Enter your enterCompanyTaxEBITDA" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.error.invalidNumeric = The enterCompanyTaxEBITDA must be valid decimal or whole number" >> ../conf/messages.en
echo "enterCompanyTaxEBITDA.error.outOfRange = EnterCompanyTaxEBITDA must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# EnterCompanyTaxEBITDAPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.title = EnterCompanyTaxEBITDA" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.heading = EnterCompanyTaxEBITDA" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.label=Enter company's Tax-EBITDA" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.checkYourAnswersLabel = EnterCompanyTaxEBITDA" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.error.nonNumeric = Enter your enterCompanyTaxEBITDA using numbers" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.error.required = Enter your enterCompanyTaxEBITDA" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.error.invalidNumeric = The enterCompanyTaxEBITDA must be valid decimal or whole number" >> ../conf/messages.cy
echo "enterCompanyTaxEBITDA.error.outOfRange = EnterCompanyTaxEBITDA must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEnterCompanyTaxEBITDAUserAnswersEntry: Arbitrary[(EnterCompanyTaxEBITDAPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[EnterCompanyTaxEBITDAPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryEnterCompanyTaxEBITDAPage: Arbitrary[EnterCompanyTaxEBITDAPage.type] =";\
    print "    Arbitrary(EnterCompanyTaxEBITDAPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(EnterCompanyTaxEBITDAPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def enterCompanyTaxEBITDA: Option[SummaryListRow] = answer(EnterCompanyTaxEBITDAPage, ukCompaniesRoutes.EnterCompanyTaxEBITDAController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    EnterCompanyTaxEBITDAPage.toString -> EnterCompanyTaxEBITDAPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    EnterCompanyTaxEBITDAPage.toString -> EnterCompanyTaxEBITDAPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val enterCompanyTaxEBITDA = \"Enter company's Tax-EBITDA\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/EnterCompanyTaxEBITDAControllerISpec.scala

echo "Migration EnterCompanyTaxEBITDA completed"
