#!/bin/bash

echo ""
echo "Applying migration PartnershipsReviewAnswersList"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### PartnershipsReviewAnswersList Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "PartnershipsReviewAnswersList" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.PartnershipsReviewAnswersListController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.PartnershipsReviewAnswersListController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.PartnershipsReviewAnswersListController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.PartnershipsReviewAnswersListController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# PartnershipsReviewAnswersListPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "partnershipsReviewAnswersList.title = Do you need to add another partnership?" >> ../conf/messages.en
echo "partnershipsReviewAnswersList.heading = Do you need to add another partnership?" >> ../conf/messages.en
echo "partnershipsReviewAnswersList.checkYourAnswersLabel = Do you need to add another partnership?" >> ../conf/messages.en
echo "partnershipsReviewAnswersList.error.required = Select yes if Do you need to add another partnership?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# PartnershipsReviewAnswersListPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "partnershipsReviewAnswersList.title = Do you need to add another partnership?" >> ../conf/messages.cy
echo "partnershipsReviewAnswersList.heading = Do you need to add another partnership?" >> ../conf/messages.cy
echo "partnershipsReviewAnswersList.checkYourAnswersLabel = Do you need to add another partnership?" >> ../conf/messages.cy
echo "partnershipsReviewAnswersList.error.required = Select yes if Do you need to add another partnership?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipsReviewAnswersListUserAnswersEntry: Arbitrary[(PartnershipsReviewAnswersListPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PartnershipsReviewAnswersListPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipsReviewAnswersListPage: Arbitrary[PartnershipsReviewAnswersListPage.type] =";\
    print "    Arbitrary(PartnershipsReviewAnswersListPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PartnershipsReviewAnswersListPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def partnershipsReviewAnswersList: Option[SummaryListRow] = answer(PartnershipsReviewAnswersListPage, electionsRoutes.PartnershipsReviewAnswersListController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    PartnershipsReviewAnswersListPage.toString -> PartnershipsReviewAnswersListPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    PartnershipsReviewAnswersListPage.toString -> PartnershipsReviewAnswersListPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val partnershipsReviewAnswersList = \"Do you need to add another partnership?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/PartnershipsReviewAnswersListControllerISpec.scala

echo "Migration PartnershipsReviewAnswersList completed"
