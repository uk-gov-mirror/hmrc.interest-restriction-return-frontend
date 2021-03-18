#!/bin/bash

echo ""
echo "Applying migration RestrictionAmountForAccountingPeriod"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ukCompanies.routes
echo "### RestrictionAmountForAccountingPeriod Controller" >> ../conf/ukCompanies.routes
echo "### ----------------------------------------" >> ../conf/ukCompanies.routes
export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "RestrictionAmountForAccountingPeriod" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ukCompanies.RestrictionAmountForAccountingPeriodController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName                          controllers.ukCompanies.RestrictionAmountForAccountingPeriodController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ukCompanies.routes
echo "GET        /$kebabClassName/change                   controllers.ukCompanies.RestrictionAmountForAccountingPeriodController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes
echo "POST       /$kebabClassName/change                   controllers.ukCompanies.RestrictionAmountForAccountingPeriodController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ukCompanies.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# RestrictionAmountForAccountingPeriodPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.title = RestrictionAmountForAccountingPeriod" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.heading = RestrictionAmountForAccountingPeriod" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.label= What is the restriction amount for the accounting period ending {0}?" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.checkYourAnswersLabel = RestrictionAmountForAccountingPeriod" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.error.nonNumeric = Enter your restrictionAmountForAccountingPeriod using numbers" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.error.required = Enter your restrictionAmountForAccountingPeriod" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.error.invalidNumeric = The restrictionAmountForAccountingPeriod must be valid decimal or whole number" >> ../conf/messages.en
echo "restrictionAmountForAccountingPeriod.error.outOfRange = RestrictionAmountForAccountingPeriod must be between {0} and {1}" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# RestrictionAmountForAccountingPeriodPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.title = RestrictionAmountForAccountingPeriod" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.heading = RestrictionAmountForAccountingPeriod" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.label=What is the restriction amount for the accounting period ending {0}?" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.checkYourAnswersLabel = RestrictionAmountForAccountingPeriod" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.error.nonNumeric = Enter your restrictionAmountForAccountingPeriod using numbers" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.error.required = Enter your restrictionAmountForAccountingPeriod" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.error.invalidNumeric = The restrictionAmountForAccountingPeriod must be valid decimal or whole number" >> ../conf/messages.cy
echo "restrictionAmountForAccountingPeriod.error.outOfRange = RestrictionAmountForAccountingPeriod must be between {0} and {1}" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRestrictionAmountForAccountingPeriodUserAnswersEntry: Arbitrary[(RestrictionAmountForAccountingPeriodPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RestrictionAmountForAccountingPeriodPage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRestrictionAmountForAccountingPeriodPage: Arbitrary[RestrictionAmountForAccountingPeriodPage.type] =";\
    print "    Arbitrary(RestrictionAmountForAccountingPeriodPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RestrictionAmountForAccountingPeriodPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def restrictionAmountForAccountingPeriod: Option[SummaryListRow] = answer(RestrictionAmountForAccountingPeriodPage, ukCompaniesRoutes.RestrictionAmountForAccountingPeriodController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    RestrictionAmountForAccountingPeriodPage.toString -> RestrictionAmountForAccountingPeriodPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    RestrictionAmountForAccountingPeriodPage.toString -> RestrictionAmountForAccountingPeriodPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val restrictionAmountForAccountingPeriod = \"What is the restriction amount for the accounting period ending {0}?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ukCompanies\/${kebabClassName}|g" ../generated-it/controllers/ukCompanies/RestrictionAmountForAccountingPeriodControllerISpec.scala

echo "Migration RestrictionAmountForAccountingPeriod completed"
