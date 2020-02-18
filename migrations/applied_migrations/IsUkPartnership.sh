#!/bin/bash

echo ""
echo "Applying migration IsUkPartnership"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### IsUkPartnership Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "IsUkPartnership" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.IsUkPartnershipController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.IsUkPartnershipController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.IsUkPartnershipController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.IsUkPartnershipController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# IsUkPartnershipPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "isUkPartnership.title = Is dsa a UK Partnership?" >> ../conf/messages.en
echo "isUkPartnership.heading = Is dsa a UK Partnership?" >> ../conf/messages.en
echo "isUkPartnership.checkYourAnswersLabel = Is dsa a UK Partnership?" >> ../conf/messages.en
echo "isUkPartnership.error.required = Select yes if Is dsa a UK Partnership?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# IsUkPartnershipPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "isUkPartnership.title = Is dsa a UK Partnership?" >> ../conf/messages.cy
echo "isUkPartnership.heading = Is dsa a UK Partnership?" >> ../conf/messages.cy
echo "isUkPartnership.checkYourAnswersLabel = Is dsa a UK Partnership?" >> ../conf/messages.cy
echo "isUkPartnership.error.required = Select yes if Is dsa a UK Partnership?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIsUkPartnershipUserAnswersEntry: Arbitrary[(IsUkPartnershipPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[IsUkPartnershipPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryIsUkPartnershipPage: Arbitrary[IsUkPartnershipPage.type] =";\
    print "    Arbitrary(IsUkPartnershipPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(IsUkPartnershipPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def isUkPartnership: Option[SummaryListRow] = answer(IsUkPartnershipPage, electionsRoutes.IsUkPartnershipController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    IsUkPartnershipPage.toString -> IsUkPartnershipPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    IsUkPartnershipPage.toString -> IsUkPartnershipPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val isUkPartnership = \"Is dsa a UK Partnership?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/IsUkPartnershipControllerISpec.scala

echo "Migration IsUkPartnership completed"
