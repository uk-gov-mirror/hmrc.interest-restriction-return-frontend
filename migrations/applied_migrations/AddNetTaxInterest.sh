#!/bin/bash

echo ""
echo "Applying migration AddNetTaxInterest"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### AddNetTaxInterest Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "AddNetTaxInterest" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.AddNetTaxInterestController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.AddNetTaxInterestController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.AddNetTaxInterestController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.AddNetTaxInterestController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# AddNetTaxInterestPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "addNetTaxInterest.title = Does this company have a net tax interest?" >> ../conf/messages.en
echo "addNetTaxInterest.heading = Does this company have a net tax interest?" >> ../conf/messages.en
echo "addNetTaxInterest.checkYourAnswersLabel = Does this company have a net tax interest?" >> ../conf/messages.en
echo "addNetTaxInterest.error.required = Select yes if Does this company have a net tax interest?" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddNetTaxInterestUserAnswersEntry: Arbitrary[(AddNetTaxInterestPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[AddNetTaxInterestPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryAddNetTaxInterestPage: Arbitrary[AddNetTaxInterestPage.type] =";\
    print "    Arbitrary(AddNetTaxInterestPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(AddNetTaxInterestPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def addNetTaxInterest: Option[SummaryListRow] = answer(AddNetTaxInterestPage, ukCompaniesRoutes.AddNetTaxInterestController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    AddNetTaxInterestPage.toString -> AddNetTaxInterestPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    AddNetTaxInterestPage.toString -> AddNetTaxInterestPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val addNetTaxInterest = \"Does this company have a net tax interest?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/AddNetTaxInterestControllerISpec.scala

echo "Migration AddNetTaxInterest completed"
