#!/bin/bash

echo ""
echo "Applying migration RevisingReturn"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /revisingReturn                        controllers.aboutReturn.RevisingReturnController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /revisingReturn                        controllers.aboutReturn.RevisingReturnController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeRevisingReturn                  controllers.aboutReturn.RevisingReturnController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeRevisingReturn                  controllers.aboutReturn.RevisingReturnController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# RevisingReturnPage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "revisingReturn.title = Are you revising a return you have already submitted?" >> ../conf/messages.en
echo "revisingReturn.heading = Are you revising a return you have already submitted?" >> ../conf/messages.en
echo "revisingReturn.checkYourAnswersLabel = Are you revising a return you have already submitted?" >> ../conf/messages.en
echo "revisingReturn.error.required = Select yes if Are you revising a return you have already submitted?" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# RevisingReturnPage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "revisingReturn.title = Are you revising a return you have already submitted?" >> ../conf/messages.cy
echo "revisingReturn.heading = Are you revising a return you have already submitted?" >> ../conf/messages.cy
echo "revisingReturn.checkYourAnswersLabel = Are you revising a return you have already submitted?" >> ../conf/messages.cy
echo "revisingReturn.error.required = Select yes if Are you revising a return you have already submitted?" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRevisingReturnUserAnswersEntry: Arbitrary[(RevisingReturnPage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[RevisingReturnPage.type]";\
    print "        value <- arbitrary[Boolean].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryRevisingReturnPage: Arbitrary[RevisingReturnPage.type] =";\
    print "    Arbitrary(RevisingReturnPage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(RevisingReturnPage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def revisingReturn: Option[SummaryListRow] = answer(RevisingReturnPage, routes.RevisingReturnController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object RevisingReturnTemplate extends WithName(\"revisingReturn.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration RevisingReturn completed"
