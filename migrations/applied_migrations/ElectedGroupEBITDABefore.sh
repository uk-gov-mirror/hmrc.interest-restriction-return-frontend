#!/bin/bash

echo ""
echo "Applying migration ElectedGroupEBITDABefore"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### ElectedGroupEBITDABefore Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "ElectedGroupEBITDABefore" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.ElectedGroupEBITDABeforeController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName                          controllers.elections.ElectedGroupEBITDABeforeController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /$kebabClassName/change                   controllers.elections.ElectedGroupEBITDABeforeController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /$kebabClassName/change                   controllers.elections.ElectedGroupEBITDABeforeController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# ElectedGroupEBITDABeforePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "electedGroupEBITDABefore.title = Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.en
echo "electedGroupEBITDABefore.heading = Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.en
echo "electedGroupEBITDABefore.checkYourAnswersLabel = Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.en
echo "electedGroupEBITDABefore.error.required = Select yes if Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# ElectedGroupEBITDABeforePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "electedGroupEBITDABefore.title = Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.cy
echo "electedGroupEBITDABefore.heading = Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.cy
echo "electedGroupEBITDABefore.checkYourAnswersLabel = Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.cy
echo "electedGroupEBITDABefore.error.required = Select yes if Has the group made a group-EBITDA (chargeable gains) election in any previous returns?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryElectedGroupEBITDABeforeUserAnswersEntry: Arbitrary[(ElectedGroupEBITDABeforePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[ElectedGroupEBITDABeforePage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryElectedGroupEBITDABeforePage: Arbitrary[ElectedGroupEBITDABeforePage.type] =";\
    print "    Arbitrary(ElectedGroupEBITDABeforePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(ElectedGroupEBITDABeforePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def electedGroupEBITDABefore: Option[SummaryListRow] = answer(ElectedGroupEBITDABeforePage, routes.ElectedGroupEBITDABeforeController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    ElectedGroupEBITDABeforePage.toString -> ElectedGroupEBITDABeforePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    ElectedGroupEBITDABeforePage.toString -> ElectedGroupEBITDABeforePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val electedGroupEBITDABefore = \"Has the group made a group-EBITDA (chargeable gains) election in any previous returns?\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/ElectedGroupEBITDABeforeControllerISpec.scala

echo "Migration ElectedGroupEBITDABefore completed"
