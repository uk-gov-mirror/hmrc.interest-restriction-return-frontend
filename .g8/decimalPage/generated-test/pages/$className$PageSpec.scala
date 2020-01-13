package pages

import pages.behaviours.PageBehaviours

class $className$PageSpec extends PageBehaviours {

  "$className$Page" must {

    beRetrievable[BigDecimal]($className$Page)

    beSettable[BigDecimal]($className$Page)

    beRemovable[BigDecimal]($className$Page)
  }
}
