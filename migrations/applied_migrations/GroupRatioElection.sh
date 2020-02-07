#!/bin/bash

echo ""
echo "Applying migration GroupRatioElection"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### GroupRatioElection Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "GroupRatioElection" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.GroupRatioElectionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.GroupRatioElectionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.GroupRatioElectionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.GroupRatioElectionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# GroupRatioElectionPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupRatioElection.title = Are you making a group ratio election?" >> ../conf/messages.en
echo "groupRatioElection.heading = Are you making a group ratio election?" >> ../conf/messages.en
echo "groupRatioElection.checkYourAnswersLabel = Are you making a group ratio election?" >> ../conf/messages.en
echo "groupRatioElection.error.required = Select yes if Are you making a group ratio election?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# GroupRatioElectionPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupRatioElection.title = Are you making a group ratio election?" >> ../conf/messages.cy
echo "groupRatioElection.heading = Are you making a group ratio election?" >> ../conf/messages.cy
echo "groupRatioElection.checkYourAnswersLabel = Are you making a group ratio election?" >> ../conf/messages.cy
echo "groupRatioElection.error.required = Select yes if Are you making a group ratio election?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupRatioElectionUserAnswersEntry: Arbitrary[(GroupRatioElectionPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupRatioElectionPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupRatioElectionPage: Arbitrary[GroupRatioElectionPage.type] =";\
    print "    Arbitrary(GroupRatioElectionPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupRatioElectionPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupRatioElection: Option[SummaryListRow] = answer(GroupRatioElectionPage, routes.GroupRatioElectionController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    GroupRatioElectionPage.toString -> GroupRatioElectionPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    GroupRatioElectionPage.toString -> GroupRatioElectionPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val groupRatioElection = \"Are you making a group ratio election?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/GroupRatioElectionControllerISpec.scala

echo "Migration GroupRatioElection completed"
