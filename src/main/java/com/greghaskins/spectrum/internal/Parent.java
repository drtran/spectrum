package com.greghaskins.spectrum.internal;

import com.greghaskins.spectrum.internal.hooks.Hooks;

public interface Parent {

  void focus(Child child);

  boolean isIgnored();

  Hooks getInheritableHooks();

  Parent NONE = new Parent() {
    @Override
    public void focus(final Child child) {}

    @Override
    public boolean isIgnored() {
      return false;
    }

    @Override
    public Hooks getInheritableHooks() {
      return new Hooks();
    }
  };
}
