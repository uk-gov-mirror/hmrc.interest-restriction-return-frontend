#!/bin/bash

echo ""
echo "Applying migration RestrictionAmountSameAP"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### RestrictionAmountSameAP Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "RestrictionAmountSameAP" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.RestrictionAmountSameAPController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.RestrictionAmountSameAPController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.RestrictionAmountSameAPController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.RestrictionAmountSameAPController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# RestrictionAmountSameAPPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "restrictionAmountSameAP.title = RestrictionAmountSameAP" >> ../conf/messages.en
echo "restrictionAmountSameAP.heading = RestrictionAmountSameAP" >> ../conf/messages.en
echo "restrictionAmountSameAP.label= Enter the restriction amount for this company" >> ../conf/messages.en
echo "restrictionAmountSameAP.checkYourAnswersLabel = RestrictionAmountSameAP" >> ../conf/messages.en
echo "restrictionAmountSameAP.error.nonNumeric = Enter your restrictionAmountSameAP using numbers" >> ../conf/messages.en
echo "restrictionAmountSameAP.error.required = Enter your restrictionAmountSameAP" >> ../conf/messages.en
echo "restrictionAmountSameAP.error.invalidNumeric = The restrictionAmountSameAP must be valid decimal or whole number" >> ../conf/messages.en
echo "restrictionAmountSameAP.error.outOfRange = RestrictionAmountSameAP must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# RestrictionAmountSameAPPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "restrictionAmountSameAP.title = RestrictionAmountSameAP" >> ../conf/messages.cy
echo "restrictionAmountSameAP.heading = RestrictionAmountSameAP" >> ../conf/messages.cy
echo "restrictionAmountSameAP.label=Enter the restriction amount for this company" >> ../conf/messages.cy
echo "restrictionAmountSameAP.checkYourAnswersLabel = RestrictionAmountSameAP" >> ../conf/messages.cy
echo "restrictionAmountSameAP.error.nonNumeric = Enter your restrictionAmountSameAP using numbers" >> ../conf/messages.cy
echo "restrictionAmountSameAP.error.required = Enter your restrictionAmountSameAP" >> ../conf/messages.cy
echo "restrictionAmountSameAP.error.invalidNumeric = The restrictionAmountSameAP must be valid decimal or whole number" >> ../conf/messages.cy
echo "restrictionAmountSameAP.error.outOfRange = RestrictionAmountSameAP must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRestrictionAmountSameAPUserAnswersEntry: Arbitrary[(RestrictionAmountSameAPPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RestrictionAmountSameAPPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRestrictionAmountSameAPPage: Arbitrary[RestrictionAmountSameAPPage.type] =";\
    print "    Arbitrary(RestrictionAmountSameAPPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RestrictionAmountSameAPPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def restrictionAmountSameAP: Option[SummaryListRow] = answer(RestrictionAmountSameAPPage, ukCompaniesRoutes.RestrictionAmountSameAPController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    RestrictionAmountSameAPPage.toString -> RestrictionAmountSameAPPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    RestrictionAmountSameAPPage.toString -> RestrictionAmountSameAPPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val restrictionAmountSameAP = \"Enter the restriction amount for this company\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/RestrictionAmountSameAPControllerISpec.scala

echo "Migration RestrictionAmountSameAP completed"
