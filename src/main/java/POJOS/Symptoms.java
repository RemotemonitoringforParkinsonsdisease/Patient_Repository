package POJOS;

/**
 * Enumeration representing the possible symptoms that can appear
 * in a medical report. These symptoms are used to describe the
 * patient's condition and are included in each {@link Report}.
 *
 * Each symptom corresponds to a common clinical sign related to
 * neurological or motor disorders.
 */
public enum Symptoms {

    /** Uncontrolled shaking or trembling of limbs. */
    TREMOR,

    /** Slowness of movement (bradykinesia). */
    SLOW_MOVEMENT,

    /** Muscle stiffness or rigidity. */
    STIFF_MUSCLES,

    /** Difficulty maintaining balance or posture. */
    DIFFICULTY_WITH_BALANCE,

    /** Short, dragging steps when walking. */
    SHUFFLING_WALK,

    /** Abnormally small and cramped handwriting (micrographia). */
    SMALL_HANDWRITING,

    /** Low or soft speaking voice. */
    SOFT_VOICE,

    /** Difficulty swallowing (dysphagia). */
    DIFFICULTY_SWALLOWING,

    /** Reduced frequency of bowel movements. */
    CONSTIPATION,

    /** Feeling faint or dizzy, often due to low blood pressure. */
    DIZZINESS_OR_FAINTING,

    /** Difficulty sleeping or irregular sleep patterns. */
    SLEEP_PROBLEMS,

    /** Persistent feelings of sadness or hopelessness. */
    DEPRESSION,

    /** Feelings of worry, nervousness, or fear. */
    ANXIETY,

    /** Reduced ability to show facial expressions (masked facies). */
    REDUCED_FACIAL_EXPRESSION,

    /** Problems controlling bladder function. */
    BLADDER_PROBLEMS
}
