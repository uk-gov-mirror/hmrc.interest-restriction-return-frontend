#!/bin/bash

echo ""
echo "Applying migration PartnershipSAUTR"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### PartnershipSAUTR Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "PartnershipSAUTR" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.PartnershipSAUTRController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.PartnershipSAUTRController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.PartnershipSAUTRController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.PartnershipSAUTRController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# PartnershipSAUTRPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "partnershipSAUTR.title = partnershipSAUTR" >> ../conf/messages.en
echo "partnershipSAUTR.heading = partnershipSAUTR" >> ../conf/messages.en
echo "partnershipSAUTR.checkYourAnswersLabel = partnershipSAUTR" >> ../conf/messages.en
echo "partnershipSAUTR.label= Enter dsa's Self Assessment Unique Taxpayer Reference" >> ../conf/messages.en
echo "partnershipSAUTR.error.required = Enter partnershipSAUTR" >> ../conf/messages.en
echo "partnershipSAUTR.error.regexp = PartnershipSAUTR must be 10 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# PartnershipSAUTRPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "partnershipSAUTR.title = partnershipSAUTR" >> ../conf/messages.cy
echo "partnershipSAUTR.heading = partnershipSAUTR" >> ../conf/messages.cy
echo "partnershipSAUTR.checkYourAnswersLabel = partnershipSAUTR" >> ../conf/messages.cy
echo "partnershipSAUTR.label = Enter dsa's Self Assessment Unique Taxpayer Reference" >> ../conf/messages.cy
echo "partnershipSAUTR.error.required = Enter partnershipSAUTR" >> ../conf/messages.cy
echo "partnershipSAUTR.error.regexp = PartnershipSAUTR must be 10 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipSAUTRUserAnswersEntry: Arbitrary[(PartnershipSAUTRPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[PartnershipSAUTRPage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryPartnershipSAUTRPage: Arbitrary[PartnershipSAUTRPage.type] =";\
    print "    Arbitrary(PartnershipSAUTRPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(PartnershipSAUTRPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def partnershipSAUTR: Option[SummaryListRow] = answer(PartnershipSAUTRPage, electionsRoutes.PartnershipSAUTRController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    PartnershipSAUTRPage.toString -> PartnershipSAUTRPage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    PartnershipSAUTRPage.toString -> PartnershipSAUTRPage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val partnershipSAUTR = \"Enter dsa's Self Assessment Unique Taxpayer Reference\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/PartnershipSAUTRControllerISpec.scala

echo "Migration PartnershipSAUTR completed"
