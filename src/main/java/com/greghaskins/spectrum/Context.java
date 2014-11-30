package com.greghaskins.spectrum;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import com.greghaskins.spectrum.Spectrum.Block;

class Context implements Executable {

    private final Description description;
    private final List<Block> setupBlocks;
    private final List<Block> teardownBlocks;
    private final List<RunOnceBlock> fixtureSetupBlocks;
    private final List<Executable> children;

    public Context(final Description description) {
        this.description = description;
        setupBlocks = new ArrayList<Block>();
        teardownBlocks = new ArrayList<Block>();
        fixtureSetupBlocks = new ArrayList<RunOnceBlock>();

        children = new ArrayList<Executable>();
    }

    @Override
    public void execute(final RunNotifier notifier) {
        for (final Executable child : children) {
            child.execute(notifier);
        }
    }

    public void addSetup(final Block block) {
        setupBlocks.add(block);
    }

    public void addTeardown(final Block block) {
        teardownBlocks.add(block);
    }

    public void addFixtureSetup(final Block block) {
        fixtureSetupBlocks.add(new RunOnceBlock(block));
    }

    public void addTest(final String behavior, final Block block) {
        final Description testDescription = Description.createTestDescription(description.getClassName(), behavior);
        final CompositeBlock testBlock = putTestBlockInContext(block);
        final Test test = new Test(testDescription, testBlock);
        description.addChild(testDescription);
        children.add(test);
    }

    private CompositeBlock putTestBlockInContext(final Block testBlock) {
        return new CompositeBlock(new CompositeBlock(fixtureSetupBlocks), new CompositeBlock(setupBlocks), testBlock,
                new CompositeBlock(teardownBlocks));
    }

    public void addChild(final Context childContext) {
        description.addChild(childContext.description);
        childContext.addSetup(new CompositeBlock(setupBlocks));
        childContext.addFixtureSetup(new CompositeBlock(fixtureSetupBlocks));
        children.add(childContext);
    }

}
