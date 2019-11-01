#!/bin/bash

echo ""
echo "Applying migration testIntPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /testIntPage                  controllers.testIntPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /testIntPage                  controllers.testIntPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changetestIntPage                        controllers.testIntPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changetestIntPage                        controllers.testIntPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "testIntPage.title = testIntPage" >> ../conf/messages.en
echo "testIntPage.heading = testIntPage" >> ../conf/messages.en
echo "testIntPage.checkYourAnswersLabel = testIntPage" >> ../conf/messages.en
echo "testIntPage.error.nonNumeric = Enter your testIntPage using numbers" >> ../conf/messages.en
echo "testIntPage.error.required = Enter your testIntPage" >> ../conf/messages.en
echo "testIntPage.error.wholeNumber = Enter your testIntPage using whole numbers" >> ../conf/messages.en
echo "testIntPage.error.outOfRange = testIntPage must be between {0} and {1}" >> ../conf/messages.en

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarytestIntPageUserAnswersEntry: Arbitrary[(testIntPagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[testIntPagePage.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrarytestIntPagePage: Arbitrary[testIntPagePage.type] =";\
    print "    Arbitrary(testIntPagePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(testIntPagePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def testIntPage: Option[AnswerRow] = userAnswers.get(testIntPagePage) map {";\
     print "    x =>";\
     print "      AnswerRow(";\
     print "        HtmlFormat.escape(messages(\"testIntPage.checkYourAnswersLabel\")),";\
     print "        HtmlFormat.escape(x.toString),";\
     print "        routes.testIntPageController.onPageLoad(CheckMode).url";\
     print "      )"
     print "  }";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Migration testIntPage completed"
