#!/bin/bash

echo ""
echo "Applying migration MyNewStringPage"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/elections.routes
echo "### MyNewStringPage Controller" >> ../conf/elections.routes
echo "### ----------------------------------------" >> ../conf/elections.routes

export kebabClassName=$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "MyNewStringPage" | tr '[:upper:]' '[:lower:]')
echo "GET        /$kebabClassName                          controllers.elections.MyNewStringPageController.onPageLoad(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName                          controllers.elections.MyNewStringPageController.onSubmit(mode: Mode = NormalMode)" >> ../conf/elections.routes
echo "GET        /$kebabClassName/change                   controllers.elections.MyNewStringPageController.onPageLoad(mode: Mode = CheckMode)" >> ../conf/elections.routes
echo "POST       /$kebabClassName/change                   controllers.elections.MyNewStringPageController.onSubmit(mode: Mode = CheckMode)" >> ../conf/elections.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# MyNewStringPagePage Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "myNewStringPage.title = myNewStringPage" >> ../conf/messages.en
echo "myNewStringPage.heading = myNewStringPage" >> ../conf/messages.en
echo "myNewStringPage.checkYourAnswersLabel = myNewStringPage" >> ../conf/messages.en
echo "myNewStringPage.label= MyNewPage" >> ../conf/messages.en
echo "myNewStringPage.error.required = Enter myNewStringPage" >> ../conf/messages.en
echo "myNewStringPage.error.length = MyNewStringPage must be 100 characters or less" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# MyNewStringPagePage Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "myNewStringPage.title = myNewStringPage" >> ../conf/messages.cy
echo "myNewStringPage.heading = myNewStringPage" >> ../conf/messages.cy
echo "myNewStringPage.checkYourAnswersLabel = myNewStringPage" >> ../conf/messages.cy
echo "myNewStringPage.label = MyNewPage" >> ../conf/messages.cy
echo "myNewStringPage.error.required = Enter myNewStringPage" >> ../conf/messages.cy
echo "myNewStringPage.error.length = MyNewStringPage must be 100 characters or less" >> ../conf/messages.cy

echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMyNewStringPageUserAnswersEntry: Arbitrary[(MyNewStringPagePage.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[MyNewStringPagePage.type]";\
    print "        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitraryMyNewStringPagePage: Arbitrary[MyNewStringPagePage.type] =";\
    print "    Arbitrary(MyNewStringPagePage)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[(MyNewStringPagePage.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo "Adding helper method to CheckYourAnswersHelper"
awk '/class/ {\
     print;\
     print "";\
     print "  def myNewStringPage: Option[SummaryListRow] = answer(MyNewStringPagePage, electionsRoutes.MyNewStringPageController.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    MyNewStringPagePage.toString -> MyNewStringPagePage,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    MyNewStringPagePage.toString -> MyNewStringPagePage,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val myNewStringPage = \"myNewStringPage\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|elections\/${kebabClassName}|g" ../generated-it/controllers/elections/MyNewStringPageControllerISpec.scala

echo "Migration MyNewStringPage completed"
