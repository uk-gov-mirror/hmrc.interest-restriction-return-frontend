#!/bin/bash

echo ""
echo "Applying migration GroupEBITDAChargeableGainsElection"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### GroupEBITDAChargeableGainsElection Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "GroupEBITDAChargeableGainsElection" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.GroupEBITDAChargeableGainsElectionController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.GroupEBITDAChargeableGainsElectionController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.GroupEBITDAChargeableGainsElectionController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.GroupEBITDAChargeableGainsElectionController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# GroupEBITDAChargeableGainsElectionPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupEBITDAChargeableGainsElection.title = Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.en
echo "groupEBITDAChargeableGainsElection.heading = Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.en
echo "groupEBITDAChargeableGainsElection.checkYourAnswersLabel = Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.en
echo "groupEBITDAChargeableGainsElection.error.required = Select yes if Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# GroupEBITDAChargeableGainsElectionPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupEBITDAChargeableGainsElection.title = Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.cy
echo "groupEBITDAChargeableGainsElection.heading = Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.cy
echo "groupEBITDAChargeableGainsElection.checkYourAnswersLabel = Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.cy
echo "groupEBITDAChargeableGainsElection.error.required = Select yes if Do you want to make a group-EBITDA (chargeable gains) election for this return?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupEBITDAChargeableGainsElectionUserAnswersEntry: Arbitrary[(GroupEBITDAChargeableGainsElectionPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupEBITDAChargeableGainsElectionPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupEBITDAChargeableGainsElectionPage: Arbitrary[GroupEBITDAChargeableGainsElectionPage.type] =";\
    print "    Arbitrary(GroupEBITDAChargeableGainsElectionPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupEBITDAChargeableGainsElectionPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupEBITDAChargeableGainsElection: Option[SummaryListRow] = answer(GroupEBITDAChargeableGainsElectionPage, routes.GroupEBITDAChargeableGainsElectionController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    GroupEBITDAChargeableGainsElectionPage.toString -> GroupEBITDAChargeableGainsElectionPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    GroupEBITDAChargeableGainsElectionPage.toString -> GroupEBITDAChargeableGainsElectionPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val groupEBITDAChargeableGainsElection = \"Do you want to make a group-EBITDA (chargeable gains) election for this return?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/GroupEBITDAChargeableGainsElectionControllerISpec.scala

echo "Migration GroupEBITDAChargeableGainsElection completed"
