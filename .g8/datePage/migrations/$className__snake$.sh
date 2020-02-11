#!/bin/bash

echo ""
echo "Applying migration $className;format="snake"$"

echo "Adding routes to conf/app.routes"

echo "" >> ../conf/app.routes
echo "### $className;format="cap"$ Controller" >> ../conf/app.routes
echo "### ----------------------------------------" >> ../conf/app.routes
export kebabClassName=\$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "$className$" | tr '[:upper:]' '[:lower:]')
echo "GET        /\$kebabClassName                          controllers.$className;format="cap"$Controller.onPageLoad(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "POST       /\$kebabClassName                          controllers.$className;format="cap"$Controller.onSubmit(mode: Mode = NormalMode)" >> ../conf/app.routes
echo "GET        /\$kebabClassName/change                   controllers.$className;format="cap"$Controller.onPageLoad(mode: Mode = CheckMode)" >> ../conf/app.routes
echo "POST       /\$kebabClassName/change                   controllers.$className;format="cap"$Controller.onSubmit(mode: Mode = CheckMode)" >> ../conf/app.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# $className;format="cap"$Page Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "$className;format="decap"$.title = $className;format="cap"$" >> ../conf/messages.en
echo "$className;format="decap"$.heading = $className;format="cap"$" >> ../conf/messages.en
echo "$className;format="decap"$.checkYourAnswersLabel = $className;format="cap"$" >> ../conf/messages.en
echo "$className;format="decap"$.error.required.all = Please enter the date for $className;format="decap"$" >> ../conf/messages.en
echo "$className;format="decap"$.error.required.two= The $className;format="decap"$" must include {0} and {1} >> ../conf/messages.en
echo "$className;format="decap"$.error.required = The $className;format="decap"$ must include {0}" >> ../conf/messages.en
echo "$className;format="decap"$.error.invalid = Enter a real date" >> ../conf/messages.en

echo ""
echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# $className;format="cap"$Page Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "$className;format="decap"$.title = $className;format="cap"$" >> ../conf/messages.cy
echo "$className;format="decap"$.heading = $className;format="cap"$" >> ../conf/messages.cy
echo "$className;format="decap"$.checkYourAnswersLabel = $className;format="cap"$" >> ../conf/messages.cy
echo "$className;format="decap"$.error.required.all = Enter the $className;format="decap"$" >> ../conf/messages.cy
echo "$className;format="decap"$.error.required.two= The $className;format="decap"$" must include {0} and {1} >> ../conf/messages.cy
echo "$className;format="decap"$.error.required = The $className;format="decap"$ must include {0}" >> ../conf/messages.cy
echo "$className;format="decap"$.error.invalid = Enter a real $className;format="cap"$" >> ../conf/messages.cy

echo ""
echo "Adding to UserAnswersEntryGenerators"
awk '/trait UserAnswersEntryGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrary$className;format="cap"$UserAnswersEntry: Arbitrary[($className;format="cap"$Page.type, JsValue)] =";\
    print "    Arbitrary {";\
    print "      for {";\
    print "        page  <- arbitrary[$className;format="cap"$Page.type]";\
    print "        value <- arbitrary[Int].map(Json.toJson(_))";\
    print "      } yield (page, value)";\
    print "    }";\
    next }1' ../test/generators/UserAnswersEntryGenerators.scala > tmp && mv tmp ../test/generators/UserAnswersEntryGenerators.scala

echo ""
echo "Adding to PageGenerators"
awk '/trait PageGenerators/ {\
    print;\
    print "";\
    print "  implicit lazy val arbitrary$className;format="cap"$Page: Arbitrary[$className;format="cap"$Page.type] =";\
    print "    Arbitrary($className;format="cap"$Page)";\
    next }1' ../test/generators/PageGenerators.scala > tmp && mv tmp ../test/generators/PageGenerators.scala

echo ""
echo "Adding to UserAnswersGenerator"
awk '/val generators/ {\
    print;\
    print "    arbitrary[($className;format="cap"$Page.type, JsValue)] ::";\
    next }1' ../test/generators/UserAnswersGenerator.scala > tmp && mv tmp ../test/generators/UserAnswersGenerator.scala

echo ""
echo "Adding helper method to CheckYourAnswersHelper"
awk '/class CheckYourAnswersHelper/ {\
     print;\
     print "  def $className;format="decap"$: Option[SummaryListRow] = answer($className;format="cap"$Page, $section;format="decap"$Routes.$className;format="cap"$Controller.onPageLoad(CheckMode))";\
     next }1' ../app/utils/CheckYourAnswersHelper.scala > tmp && mv tmp ../app/utils/CheckYourAnswersHelper.scala

echo "Adding to Pages map"
awk '/val pages/ {\
    print;\
    print "    $className;format="cap"$Page.toString -> $className;format="cap"$Page,";\
    next }1' ../app/pages/Page.scala > tmp && mv tmp ../app/pages/Page.scala

echo "adding to Pages map spec"
awk '/val expected/ {\
    print;\
    print "    $className;format="cap"$Page.toString -> $className;format="cap"$Page,";\
    next }1' ../test/pages/PageSpec.scala > tmp && mv tmp ../test/pages/PageSpec.scala

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val $className;format="decap"$ = \"$title$\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "s|ROUTING_PLACEHOLDER|$section$\/\${kebabClassName}|g" ../generated-it/controllers/$className$ControllerISpec.scala

echo "Migration $className;format="snake"$ completed"
