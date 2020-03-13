#!/bin/bash

echo ""
echo "Applying migration PartnershipDeletionConfirmation"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### PartnershipDeletionConfirmation Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "PartnershipDeletionConfirmation" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.PartnershipDeletionConfirmationController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.PartnershipDeletionConfirmationController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.PartnershipDeletionConfirmationController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.PartnershipDeletionConfirmationController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# PartnershipDeletionConfirmationPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "partnershipDeletionConfirmation.title = Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.en
echo "partnershipDeletionConfirmation.heading = Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.en
echo "partnershipDeletionConfirmation.checkYourAnswersLabel = Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.en
echo "partnershipDeletionConfirmation.error.required = Select yes if Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# PartnershipDeletionConfirmationPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "partnershipDeletionConfirmation.title = Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.cy
echo "partnershipDeletionConfirmation.heading = Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.cy
echo "partnershipDeletionConfirmation.checkYourAnswersLabel = Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.cy
echo "partnershipDeletionConfirmation.error.required = Select yes if Are you sure you want to delete partnership ‘{0}’?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipDeletionConfirmationUserAnswersEntry: Arbitrary[(PartnershipDeletionConfirmationPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PartnershipDeletionConfirmationPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipDeletionConfirmationPage: Arbitrary[PartnershipDeletionConfirmationPage.type] =";\
    print "    Arbitrary(PartnershipDeletionConfirmationPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PartnershipDeletionConfirmationPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def partnershipDeletionConfirmation: Option[SummaryListRow] = answer(PartnershipDeletionConfirmationPage, electionsRoutes.PartnershipDeletionConfirmationController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    PartnershipDeletionConfirmationPage.toString -> PartnershipDeletionConfirmationPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    PartnershipDeletionConfirmationPage.toString -> PartnershipDeletionConfirmationPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val partnershipDeletionConfirmation = \"Are you sure you want to delete partnership ‘{0}’?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/PartnershipDeletionConfirmationControllerISpec.scala

echo "Migration PartnershipDeletionConfirmation completed"
