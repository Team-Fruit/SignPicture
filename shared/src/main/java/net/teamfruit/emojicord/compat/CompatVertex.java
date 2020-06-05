package net.teamfruit.emojicord.compat;

import net.minecraft.client.renderer.Tessellator;
#if MC_7_LATER
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
#endif

import javax.annotation.Nonnull;

public class CompatVertex {
	private static class CompatBaseVertexImpl implements CompatBaseVertex {
		public static final @Nonnull Tessellator t = Tessellator. #if MC_7_LATER getInstance() #else instance #endif ;
		#if MC_12_OR_LATER
		public static final @Nonnull net.minecraft.client.renderer.BufferBuilder w = t.getBuffer();
		#elif MC_7_LATER
		public static final @Nonnull net.minecraft.client.renderer.VertexBuffer w = t.getBuffer();
		#endif

		public CompatBaseVertexImpl() {
		}

		@Override
		public void draw() {
			endVertex();
			t.draw();
		}

		@Override
		public @Nonnull CompatBaseVertex begin(final int mode) {
			#if MC_7_LATER
			w.begin(mode, DefaultVertexFormats.POSITION);
			#else
			t.startDrawing(mode);
			#endif
			init();
			return this;
		}

		@Override
		public @Nonnull CompatBaseVertex beginTexture(final int mode) {
			#if MC_7_LATER
			w.begin(mode, DefaultVertexFormats.POSITION_TEX);
			#else
			t.startDrawing(mode);
			#endif
			init();
			return this;
		}

		private void init() {
			this.stack = false;
		}

		private boolean stack;
		#if !MC_7_LATER
		private double stack_x;
		private double stack_y;
		private double stack_z;
		#endif

		@Override
		public @Nonnull CompatBaseVertex pos(final double x, final double y, final double z) {
			endVertex();
			#if MC_7_LATER
			w.pos(x, y, z);
			#else
			this.stack_x = x;
			this.stack_y = y;
			this.stack_z = z;
			#endif
			this.stack = true;
			return this;
		}

		@Override
		public @Nonnull CompatBaseVertex tex(final double u, final double v) {
			#if MC_14_LATER
			w.tex((float)u, (float)v);
			#elif MC_7_LATER
			w.tex(u, v);
			#else
			t.setTextureUV(u, v);
			#endif
			return this;
		}

		@Override
		public @Nonnull CompatBaseVertex color(final float red, final float green, final float blue, final float alpha) {
			return this.color((int) (red*255.0F), (int) (green*255.0F), (int) (blue*255.0F), (int) (alpha*255.0F));
		}

		@Override
		public @Nonnull CompatBaseVertex color(final int red, final int green, final int blue, final int alpha) {
			#if MC_14_LATER w.color( #elif MC_7_LATER w.putColorRGBA(0, #else t.setColorRGBA( #endif red, green, blue, alpha);
			return this;
		}

		@Override
		public @Nonnull CompatBaseVertex normal(final float nx, final float ny, final float nz) {
			#if MC_7_LATER w.normal #else t.setNormal #endif (nx, ny, nz);
			return this;
		}

		private void endVertex() {
			if (this.stack) {
				this.stack = false;
				#if MC_7_LATER
				w.endVertex();
				#else
				t.addVertex(this.stack_x, this.stack_y, this.stack_z);
				#endif
			}
		}
	}

	public static @Nonnull CompatBaseVertex getTessellator() {
		return new CompatBaseVertexImpl();
	}
}
