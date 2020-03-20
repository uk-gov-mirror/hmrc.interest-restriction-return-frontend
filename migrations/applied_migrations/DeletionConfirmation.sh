#!/bin/bash

echo ""
echo "Applying migration DeletionConfirmation"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/ultimateParentCompany.routes
echo "### DeletionConfirmation Controller" >> ../conf/ultimateParentCompany.routes
echo "### ----------------------------------------" >> ../conf/ultimateParentCompany.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "DeletionConfirmation" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.ultimateParentCompany.DeletionConfirmationController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/ultimateParentCompany.routes
echo "POST       /$kebabClassName                          controllers.ultimateParentCompany.DeletionConfirmationController.onSubmit(mode: Mode = NormalMode)" >> ../conf/ultimateParentCompany.routes
echo "GET        /$kebabClassName/change                   controllers.ultimateParentCompany.DeletionConfirmationController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/ultimateParentCompany.routes
echo "POST       /$kebabClassName/change                   controllers.ultimateParentCompany.DeletionConfirmationController.onSubmit(mode: Mode = CheckMode)" >> ../conf/ultimateParentCompany.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# DeletionConfirmationPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "deletionConfirmation.title = Are you sure you want to delete this deemed company?" >> ../conf/messages.en
echo "deletionConfirmation.heading = Are you sure you want to delete this deemed company?" >> ../conf/messages.en
echo "deletionConfirmation.checkYourAnswersLabel = Are you sure you want to delete this deemed company?" >> ../conf/messages.en
echo "deletionConfirmation.error.required = Select yes if Are you sure you want to delete this deemed company?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# DeletionConfirmationPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "deletionConfirmation.title = Are you sure you want to delete this deemed company?" >> ../conf/messages.cy
echo "deletionConfirmation.heading = Are you sure you want to delete this deemed company?" >> ../conf/messages.cy
echo "deletionConfirmation.checkYourAnswersLabel = Are you sure you want to delete this deemed company?" >> ../conf/messages.cy
echo "deletionConfirmation.error.required = Select yes if Are you sure you want to delete this deemed company?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDeletionConfirmationUserAnswersEntry: Arbitrary[(DeletionConfirmationPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[DeletionConfirmationPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryDeletionConfirmationPage: Arbitrary[DeletionConfirmationPage.type] =";\
    print "    Arbitrary(DeletionConfirmationPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(DeletionConfirmationPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def deletionConfirmation: Option[SummaryListRow] = answer(DeletionConfirmationPage, ultimateParentCompanyRoutes.DeletionConfirmationController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    DeletionConfirmationPage.toString -> DeletionConfirmationPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    DeletionConfirmationPage.toString -> DeletionConfirmationPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val deletionConfirmation = \"Are you sure you want to delete this deemed company?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|ultimateParentCompany\/${kebabClassName}|g" ../generated-it/controllers/ultimateParentCompany/DeletionConfirmationControllerISpec.scala

echo "Migration DeletionConfirmation completed"
