Feature: Inscripción a cursos
  El estudiante I2024001 ya está inscrito en el curso 1

  Scenario: Inscripción correcta
    Given un estudiante nuevo con código "I2024050"
    When intenta inscribirse en el curso 2
    Then el sistema responde con el código de estado 200

  Scenario: Inscripción duplicada
    Given que el estudiante "I2024001" ya está inscrito en el curso 1
    When intenta inscribirse nuevamente en el curso 1
    Then el sistema rechaza la inscripción con un código de error 4xx o 5xx