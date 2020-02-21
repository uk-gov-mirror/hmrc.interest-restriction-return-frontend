#!/bin/bash

echo ""
echo "Applying migration NetTaxInterestAmount"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### NetTaxInterestAmount Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "NetTaxInterestAmount" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.NetTaxInterestAmountController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.NetTaxInterestAmountController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.NetTaxInterestAmountController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.NetTaxInterestAmountController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# NetTaxInterestAmountPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "netTaxInterestAmount.title = NetTaxInterestAmount" >> ../conf/messages.en
echo "netTaxInterestAmount.heading = NetTaxInterestAmount" >> ../conf/messages.en
echo "netTaxInterestAmount.label= Enter the company's total net-tax interest income" >> ../conf/messages.en
echo "netTaxInterestAmount.checkYourAnswersLabel = NetTaxInterestAmount" >> ../conf/messages.en
echo "netTaxInterestAmount.error.nonNumeric = Enter your netTaxInterestAmount using numbers" >> ../conf/messages.en
echo "netTaxInterestAmount.error.required = Enter your netTaxInterestAmount" >> ../conf/messages.en
echo "netTaxInterestAmount.error.invalidNumeric = The netTaxInterestAmount must be valid decimal or whole number" >> ../conf/messages.en
echo "netTaxInterestAmount.error.outOfRange = NetTaxInterestAmount must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# NetTaxInterestAmountPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "netTaxInterestAmount.title = NetTaxInterestAmount" >> ../conf/messages.cy
echo "netTaxInterestAmount.heading = NetTaxInterestAmount" >> ../conf/messages.cy
echo "netTaxInterestAmount.label=Enter the company's total net-tax interest income" >> ../conf/messages.cy
echo "netTaxInterestAmount.checkYourAnswersLabel = NetTaxInterestAmount" >> ../conf/messages.cy
echo "netTaxInterestAmount.error.nonNumeric = Enter your netTaxInterestAmount using numbers" >> ../conf/messages.cy
echo "netTaxInterestAmount.error.required = Enter your netTaxInterestAmount" >> ../conf/messages.cy
echo "netTaxInterestAmount.error.invalidNumeric = The netTaxInterestAmount must be valid decimal or whole number" >> ../conf/messages.cy
echo "netTaxInterestAmount.error.outOfRange = NetTaxInterestAmount must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNetTaxInterestAmountUserAnswersEntry: Arbitrary[(NetTaxInterestAmountPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[NetTaxInterestAmountPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryNetTaxInterestAmountPage: Arbitrary[NetTaxInterestAmountPage.type] =";\
    print "    Arbitrary(NetTaxInterestAmountPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(NetTaxInterestAmountPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def netTaxInterestAmount: Option[SummaryListRow] = answer(NetTaxInterestAmountPage, ukCompaniesRoutes.NetTaxInterestAmountController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    NetTaxInterestAmountPage.toString -> NetTaxInterestAmountPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    NetTaxInterestAmountPage.toString -> NetTaxInterestAmountPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val netTaxInterestAmount = \"Enter the company's total net-tax interest income\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/NetTaxInterestAmountControllerISpec.scala

echo "Migration NetTaxInterestAmount completed"
