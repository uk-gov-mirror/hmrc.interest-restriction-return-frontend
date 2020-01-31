#!/bin/bash

echo ""
echo "Applying migration GroupSubjectToRestrictions"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /groupSubjectToRestrictions                        controllers.aboutReturn.GroupSubjectToRestrictionsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /groupSubjectToRestrictions                        controllers.aboutReturn.GroupSubjectToRestrictionsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeGroupSubjectToRestrictions                  controllers.aboutReturn.GroupSubjectToRestrictionsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeGroupSubjectToRestrictions                  controllers.aboutReturn.GroupSubjectToRestrictionsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# GroupSubjectToRestrictionsPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupSubjectToRestrictions.title = Is the group subject to restrictions?" >> ../conf/messages.en
echo "groupSubjectToRestrictions.heading = Is the group subject to restrictions?" >> ../conf/messages.en
echo "groupSubjectToRestrictions.checkYourAnswersLabel = Is the group subject to restrictions?" >> ../conf/messages.en
echo "groupSubjectToRestrictions.error.required = Select yes if Is the group subject to restrictions?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# GroupSubjectToRestrictionsPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupSubjectToRestrictions.title = Is the group subject to restrictions?" >> ../conf/messages.cy
echo "groupSubjectToRestrictions.heading = Is the group subject to restrictions?" >> ../conf/messages.cy
echo "groupSubjectToRestrictions.checkYourAnswersLabel = Is the group subject to restrictions?" >> ../conf/messages.cy
echo "groupSubjectToRestrictions.error.required = Select yes if Is the group subject to restrictions?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupSubjectToRestrictionsUserAnswersEntry: Arbitrary[(GroupSubjectToRestrictionsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupSubjectToRestrictionsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupSubjectToRestrictionsPage: Arbitrary[GroupSubjectToRestrictionsPage.type] =";\
    print "    Arbitrary(GroupSubjectToRestrictionsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupSubjectToRestrictionsPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupSubjectToRestrictions: Option[SummaryListRow] = answer(GroupSubjectToRestrictionsPage, routes.GroupSubjectToRestrictionsController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration GroupSubjectToRestrictions completed"
