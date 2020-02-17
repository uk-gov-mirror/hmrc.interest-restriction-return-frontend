#!/bin/bash

echo ""
echo "Applying migration PartnershipName"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### PartnershipName Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "PartnershipName" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.PartnershipNameController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.PartnershipNameController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.PartnershipNameController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.PartnershipNameController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# PartnershipNamePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "partnershipName.title = partnershipName" >> ../conf/messages.en
echo "partnershipName.heading = partnershipName" >> ../conf/messages.en
echo "partnershipName.checkYourAnswersLabel = partnershipName" >> ../conf/messages.en
echo "partnershipName.label= Enter the name of the partnership" >> ../conf/messages.en
echo "partnershipName.error.required = Enter partnershipName" >> ../conf/messages.en
echo "partnershipName.error.length = PartnershipName must be 160 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# PartnershipNamePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "partnershipName.title = partnershipName" >> ../conf/messages.cy
echo "partnershipName.heading = partnershipName" >> ../conf/messages.cy
echo "partnershipName.checkYourAnswersLabel = partnershipName" >> ../conf/messages.cy
echo "partnershipName.label = Enter the name of the partnership" >> ../conf/messages.cy
echo "partnershipName.error.required = Enter partnershipName" >> ../conf/messages.cy
echo "partnershipName.error.length = PartnershipName must be 160 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipNameUserAnswersEntry: Arbitrary[(PartnershipNamePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PartnershipNamePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipNamePage: Arbitrary[PartnershipNamePage.type] =";\
    print "    Arbitrary(PartnershipNamePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PartnershipNamePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def partnershipName: Option[SummaryListRow] = answer(PartnershipNamePage, electionsRoutes.PartnershipNameController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    PartnershipNamePage.toString -> PartnershipNamePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    PartnershipNamePage.toString -> PartnershipNamePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val partnershipName = \"Enter the name of the partnership\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/PartnershipNameControllerISpec.scala

echo "Migration PartnershipName completed"
