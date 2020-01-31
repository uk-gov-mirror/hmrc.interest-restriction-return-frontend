#!/bin/bash

echo ""
echo "Applying migration GroupSubjectToReactivations"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /groupSubjectToReactivations                        controllers.aboutReturn.GroupSubjectToReactivationsController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /groupSubjectToReactivations                        controllers.aboutReturn.GroupSubjectToReactivationsController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeGroupSubjectToReactivations                  controllers.aboutReturn.GroupSubjectToReactivationsController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeGroupSubjectToReactivations                  controllers.aboutReturn.GroupSubjectToReactivationsController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# GroupSubjectToReactivationsPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "groupSubjectToReactivations.title = groupSubjectToReactivations" >> ../conf/messages.en
echo "groupSubjectToReactivations.heading = groupSubjectToReactivations" >> ../conf/messages.en
echo "groupSubjectToReactivations.checkYourAnswersLabel = groupSubjectToReactivations" >> ../conf/messages.en
echo "groupSubjectToReactivations.error.required = Select yes if groupSubjectToReactivations" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# GroupSubjectToReactivationsPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "groupSubjectToReactivations.title = groupSubjectToReactivations" >> ../conf/messages.cy
echo "groupSubjectToReactivations.heading = groupSubjectToReactivations" >> ../conf/messages.cy
echo "groupSubjectToReactivations.checkYourAnswersLabel = groupSubjectToReactivations" >> ../conf/messages.cy
echo "groupSubjectToReactivations.error.required = Select yes if groupSubjectToReactivations" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupSubjectToReactivationsUserAnswersEntry: Arbitrary[(GroupSubjectToReactivationsPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[GroupSubjectToReactivationsPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryGroupSubjectToReactivationsPage: Arbitrary[GroupSubjectToReactivationsPage.type] =";\
    print "    Arbitrary(GroupSubjectToReactivationsPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(GroupSubjectToReactivationsPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def groupSubjectToReactivations: Option[SummaryListRow] = answer(GroupSubjectToReactivationsPage, routes.GroupSubjectToReactivationsController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration GroupSubjectToReactivations completed"
