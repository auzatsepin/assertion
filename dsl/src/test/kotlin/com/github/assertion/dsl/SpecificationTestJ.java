package com.github.assertion.dsl;

import com.github.assertion.context.Context;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpecificationTestJ {

    private static final String IN_CTX_PARAM = "in";
    private static final String OUT_CTX_PARAM = "out";

    @Test
    void shouldExecuteSpecificationWithContext() {
        final Context context = new Context();
        context.set(IN_CTX_PARAM, 3);
        final Specification spec = SpecificationKt.specification(specificationBuilder -> {
            specificationBuilder.action(
                    aCtx -> {
                        aCtx.set(OUT_CTX_PARAM, aCtx.get(IN_CTX_PARAM, Integer.class) * 2);
                    }
            );
            specificationBuilder.verify(oCtx -> {
                assertEquals(Integer.valueOf(6), oCtx.get(OUT_CTX_PARAM, Integer.class));
            });
            return null;
        });
        spec.with(context);
    }

    @Test
    void shouldExecuteSpecificationInitialContext() {
        final Context context = new Context();
        context.set(IN_CTX_PARAM, 3);
        final Specification spec = SpecificationKt.specification(context, specificationBuilder -> {
            specificationBuilder.verify(oCtx -> {
                assertEquals(Integer.valueOf(3), oCtx.get(IN_CTX_PARAM, Integer.class));
            });
            return null;
        });
        spec.invoke();
    }
}

