#!/bin/bash

echo ""
echo "Applying migration MyNewPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "GET        /myNewPage                        controllers.MyNewPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /myNewPage                        controllers.MyNewPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes

echo "GET        /changeMyNewPage                  controllers.MyNewPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /changeMyNewPage                  controllers.MyNewPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "" >> ../conf/messages.en
echo "# MyNewPagePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "myNewPage.title = myNewPage" >> ../conf/messages.en
echo "myNewPage.heading = myNewPage" >> ../conf/messages.en
echo "myNewPage.option1 = Option 1" >> ../conf/messages.en
echo "myNewPage.option2 = Option 2" >> ../conf/messages.en
echo "myNewPage.checkYourAnswersLabel = myNewPage" >> ../conf/messages.en
echo "myNewPage.error.required = Select myNewPage" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "" >> ../conf/messages.cy
echo "# MyNewPagePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "myNewPage.title = myNewPage" >> ../conf/messages.cy
echo "myNewPage.heading = myNewPage" >> ../conf/messages.cy
echo "myNewPage.option1 = Option 1" >> ../conf/messages.cy
echo "myNewPage.option2 = Option 2" >> ../conf/messages.cy
echo "myNewPage.checkYourAnswersLabel = myNewPage" >> ../conf/messages.cy
echo "myNewPage.error.required = Select myNewPage" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMyNewPageUserAnswersEntry: Arbitrary[(MyNewPagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MyNewPagePage.type]";\
    print "        value <- arbitrary[MyNewPage].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMyNewPagePage: Arbitrary[MyNewPagePage.type] =";\
    print "    Arbitrary(MyNewPagePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to ModelGenerators"
awk '/trait ModelGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMyNewPage: Arbitrary[MyNewPage] =";\
    print "    Arbitrary {";\
    print "      Gen.oneOf(MyNewPage.values.toSeq)";\
    print "    }";\
    next }1' ../test/generators/ModelGenerators.scala > tmp && mv tmp ../test/generators/ModelGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MyNewPagePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def myNewPage: Option[SummaryListRow] = answer(MyNewPagePage, routes.MyNewPageController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding template to Nunjucks templates"
echo "object MyNewPageTemplate extends WithName(\"myNewPage.njk\") with ViewTemplate" >> ../app/nunjucks/ViewTemplate.scala

echo "Migration MyNewPage completed"
