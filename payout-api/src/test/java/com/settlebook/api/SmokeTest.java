package com.settlebook.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest {

    @Test
    void theBuildRunsTests() {
        assertThat(1 + 1).isEqualTo(2);
    }
}