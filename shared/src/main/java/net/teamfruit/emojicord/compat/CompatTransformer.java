package net.teamfruit.emojicord.compat;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;

#if MC_12_LATER
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import java.util.Arrays;
import java.util.stream.Collectors;
#else
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.IClassTransformer;
#endif

public abstract class CompatTransformer implements #if MC_12_LATER ITransformer<ClassNode> #else IClassTransformer #endif {
	private static final Logger LOGGER = LogManager.getLogger();

	public abstract ClassNode read(@Nonnull byte[] bytes);

	public abstract byte[] write(@Nonnull ClassNode node);

	public abstract ClassNode transform(final ClassNode input, final CompatTransformerVotingContext context);

	public abstract DeferredTransform[] deferredTransforms();

	public abstract Set<String> targetNames();

	public static class CompatTransformerVotingContext {
	}

	public static class DeferredTransform {
		private final String thisname;
		private final String targetname;
		#if MC_12_LATER
		private Supplier<Boolean> shouldDeferSupplier;
		#endif

		public DeferredTransform(final String thisname, final String targetname) {
			this.thisname = thisname;
			this.targetname = targetname;
			#if MC_12_LATER
			this.shouldDeferSupplier = Suppliers.memoize(() -> {
				try {
					Class.forName(this.targetname, false, getClass().getClassLoader());
					return true;
				} catch (final ClassNotFoundException e) {
				}
				return false;
			});
			#endif
		}

		public boolean hasTarget() {
			return this. #if MC_12_LATER shouldDeferSupplier.get() #else targetfound #endif ;
		}

		#if MC_12_LATER
		public boolean shouldDefer() {
			return this.shouldDeferSupplier.get();
		}
		#else
		private boolean targetinitialized;
		private boolean targetfound;

		private Supplier<List<?>> tweakClassNamesSupplier = Suppliers.memoize(() -> {
			return (List<?>) Launch.blackboard.get("TweakClasses");
		});

		private boolean transformEqual(String transformName, String className) {
			return StringUtils.equals(transformName, className) || StringUtils.equals(transformName, "$wrapper."+className);
		}

		public void transform(final String name, final String transformedName) {
			if (transformEqual(transformedName, this.targetname))
				this.targetfound = true;

			// Ignore while transforming
			if (!tweakClassNamesSupplier.get().isEmpty())
				return;

			if (!this.targetinitialized) {
				this.targetinitialized = true;
				apply();
			}
		}

		public void apply() {
			try {
				final Field $transformers = Class.forName("net.minecraft.launchwrapper.LaunchClassLoader").getDeclaredField("transformers");
				$transformers.setAccessible(true);
				final List<?> transformers = (List<?>) $transformers.get(Class.forName("net.minecraft.launchwrapper.Launch").getField("classLoader").get(null));
				int thistransformer = -1, targettransformer = -1;
				for (final ListIterator<?> itr = transformers.listIterator(); itr.hasNext();) {
					final int index = itr.nextIndex();
					final Object transformer = itr.next();
					final String tname = transformer.getClass().getName();
					if (transformEqual(tname, this.thisname))
						thistransformer = index;
					else if (transformEqual(tname, this.targetname))
						targettransformer = index;
				}
				if (thistransformer>=0&&targettransformer>=0&&targettransformer>thistransformer) {
					Collections.swap(transformers, thistransformer, targettransformer);
					LOGGER.info("The order of EmojicordTransformer and IntelliInputTransformer has been swapped while loading");
				}
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		#endif
	}

	#if MC_12_LATER
	private boolean deferred = false;

	@Override
	public TransformerVoteResult castVote(final ITransformerVotingContext context) {
		if (this.deferred)
			return TransformerVoteResult.YES;
		this.deferred = true;
		return Arrays.stream(deferredTransforms()).anyMatch(DeferredTransform::shouldDefer) ? TransformerVoteResult.DEFER : TransformerVoteResult.YES;
	}

	@Override
	public Set<Target> targets() {
		return targetNames().stream().map(Target::targetClass).collect(Collectors.toSet());
	}

	@Override
	public ClassNode transform(final ClassNode input, final ITransformerVotingContext context) {
		return transform(input, new CompatTransformerVotingContext());
	}
	#else
	@Override
	public byte[] transform(final String name, final String transformedName, byte[] bytes) {
		if (bytes==null||name==null||transformedName==null)
			return bytes;

		for (final DeferredTransform transform : deferredTransforms())
			transform.transform(name, transformedName);

		if (targetNames().contains(transformedName))
			try {
				ClassNode node = read(bytes);

				node = transform(node, new CompatTransformerVotingContext());

				bytes = write(node);
			} catch (final Exception e) {
				LOGGER.fatal("Could not transform: ", e);
			}

		return bytes;
	}
	#endif
}
