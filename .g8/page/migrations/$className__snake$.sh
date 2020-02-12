#!/bin/bash

echo ""
echo "Applying migration $className;format="snake"$"

echo "" >> ../conf/$section;format="decap"$.routes
echo "### $className;format="cap"$ Controller" >> ../conf/$section;format="decap"$.routes
echo "### ----------------------------------------" >> ../conf/$section;format="decap"$.routes
export kebabClassName=\$(sed -e 's/\([^A-Z]\)\([A-Z0-9]\)/\1-\2/g' -e 's/\([A-Z0-9]\)\([A-Z0-9]\)\([^A-Z]\)/\1-\2\3/g' <<< "$className$" | tr '[:upper:]' '[:lower:]')
echo "GET        /\$kebabClassName                         controllers.$section;format="decap"$.$className;format="cap"$Controller.onPageLoad()" >> ../conf/$section;format="decap"$.routes

echo "Adding messages to English conf.messages"
echo "" >> ../conf/messages.en
echo "# $className;format="cap"$Page Messages" >> ../conf/messages.en
echo "# ----------------------------------------------------------" >> ../conf/messages.en
echo "$className;format="decap"$.title = $className;format="decap"$" >> ../conf/messages.en
echo "$className;format="decap"$.heading = $className;format="decap"$" >> ../conf/messages.en

echo "Adding messages to Welsh conf.messages"
echo "" >> ../conf/messages.cy
echo "# $className;format="cap"$Page Messages" >> ../conf/messages.cy
echo "# ----------------------------------------------------------" >> ../conf/messages.cy
echo "$className;format="decap"$.title = $className;format="decap"$" >> ../conf/messages.cy
echo "$className;format="decap"$.heading = $className;format="decap"$" >> ../conf/messages.cy

echo "adding to PageTitles"
awk '/object PageTitles/ {\
    print;\
    print "  val $className;format="decap"$ = \"$title$\"";\
    next }1' ../it/assets/PageTitles.scala > tmp && mv tmp ../it/assets/PageTitles.scala

echo "adding route to integration test"

sed -i "" "s|ROUTING_PLACEHOLDER|$section;format="decap"$\/\${kebabClassName}|g" ../generated-it/controllers/$section;format="decap"$/$className$ControllerISpec.scala

echo "Migration $className;format="snake"$ completed"
