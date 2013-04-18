package nz.net.initial3d.renderer;

/**
 *
 * Unsafe struct type field offsets and constants.
 *
 * @author Ben Allen
 *
 */
final class Type {

	private Type() {
		throw new AssertionError();
	}

	static final class i3d_t {

		private i3d_t() {
			throw new AssertionError();
		}

		/** Constant: size in bytes of an instance of this type */
		static int SIZEOF() {
			return 9001;
		}

	}

	static final class light_t {

		private light_t() {
			throw new AssertionError();
		}

		/** Constant: size in bytes of an instance of this type */
		static int SIZEOF() {
			return 256;
		}

		/** Field type: int; 0x1 => enabled, 0x2 => last */
		static int flags() {
			return 0;
		}

		/** Field type: float */
		static int ia_a() {
			return 4;
		}

		/** Field type: float */
		static int ia_r() {
			return 8;
		}

		/** Field type: float */
		static int ia_g() {
			return 12;
		}

		/** Field type: float */
		static int ia_b() {
			return 16;
		}

		/** Field type: float */
		static int id_a() {
			return 20;
		}

		/** Field type: float */
		static int id_r() {
			return 24;
		}

		/** Field type: float */
		static int id_g() {
			return 28;
		}

		/** Field type: float */
		static int id_b() {
			return 32;
		}

		/** Field type: float */
		static int is_a() {
			return 36;
		}

		/** Field type: float */
		static int is_r() {
			return 40;
		}

		/** Field type: float */
		static int is_g() {
			return 44;
		}

		/** Field type: float */
		static int is_b() {
			return 48;
		}

		// note: .pos and .dir stored in view space so no need for double

		/** Field type: float */
		static int pos_x() {
			return 52;
		}

		/** Field type: float */
		static int pos_y() {
			return 56;
		}

		/** Field type: float */
		static int pos_z() {
			return 60;
		}

		/** Field type: float */
		static int pos_w() {
			return 64;
		}

		/** Field type: float */
		static int dir_x() {
			return 68;
		}

		/** Field type: float */
		static int dir_y() {
			return 72;
		}

		/** Field type: float */
		static int dir_z() {
			return 76;
		}

		/** Field type: float */
		static int dir_w() {
			return 80;
		}

		/** Field type: float */
		static int constant_attenuation() {
			return 84;
		}

		/** Field type: float */
		static int linear_attenuation() {
			return 88;
		}

		/** Field type: float */
		static int quadratic_attenuation() {
			return 92;
		}

		/** Field type: float */
		static int spot_cutoff() {
			return 96;
		}

		/** Field type: float */
		static int spot_exp() {
			return 100;
		}

		/** Field type: float */
		static int inv_effect_rad() {
			return 104;
		}

	}

	static final class material_t {

		private material_t() {
			throw new AssertionError();
		}

		/** Constant: size in bytes of an instance of this type */
		static int SIZEOF() {
			return 256;
		}

		static int ka_a_unused() {
			return 0;
		}

		/** Field type: float */
		static int ka_r() {
			return 4;
		}

		/** Field type: float */
		static int ka_g() {
			return 8;
		}

		/** Field type: float */
		static int ka_b() {
			return 12;
		}

		/** Field type: float */
		static int kd_a_opacity() {
			return 16;
		}

		/** Field type: float */
		static int kd_r() {
			return 20;
		}

		/** Field type: float */
		static int kd_g() {
			return 24;
		}

		/** Field type: float */
		static int kd_b() {
			return 28;
		}

		/** Field type: float */
		static int ks_a_shininess() {
			return 32;
		}

		/** Field type: float */
		static int ks_r() {
			return 36;
		}

		/** Field type: float */
		static int ks_g() {
			return 40;
		}

		/** Field type: float */
		static int ks_b() {
			return 44;
		}

		static int ke_a_unused() {
			return 48;
		}

		/** Field type: float */
		static int ke_r() {
			return 52;
		}

		/** Field type: float */
		static int ke_g() {
			return 56;
		}

		/** Field type: float */
		static int ke_b() {
			return 60;
		}

		/** Field type: pointer */
		static int pMap_kd() {
			return 64;
		}

		/** Field type: pointer */
		static int pMap_ks() {
			return 72;
		}

		/** Field type: pointer */
		static int pMap_ke() {
			return 80;
		}

	}

	static final class tri_t {

		private tri_t() {
			throw new AssertionError();
		}

		/** Constant: size in bytes of an instance of this type */
		static int SIZEOF() {
			return 196;
		}

		/** Field type: int; 0x1 => deleted, 0x2 => cw, else ccw */
		static int flags() {
			return 0;
		}

		/** Field type: pointer */
		static int pv0() {
			return 4;
		}

		/** Field type: pointer */
		static int pvt0() {
			return 12;
		}

		/** Field type: pointer */
		static int pvn0() {
			return 20;
		}

		/** Field type: pointer */
		static int pvv0() {
			return 28;
		}

		/** Field type: float */
		static int v0_c0_a() {
			return 36;
		}

		/** Field type: float */
		static int v0_c0_r() {
			return 40;
		}

		/** Field type: float */
		static int v0_c0_g() {
			return 44;
		}

		/** Field type: float */
		static int v0_c0_b() {
			return 48;
		}

		/** Field type: float */
		static int v0_c1_a() {
			return 52;
		}

		/** Field type: float */
		static int v0_c1_r() {
			return 56;
		}

		/** Field type: float */
		static int v0_c1_g() {
			return 60;
		}

		/** Field type: float */
		static int v0_c1_b() {
			return 64;
		}

		/** Field type: pointer */
		static int pv1() {
			return 68;
		}

		/** Field type: pointer */
		static int pvt1() {
			return 76;
		}

		/** Field type: pointer */
		static int pvn1() {
			return 84;
		}

		/** Field type: pointer */
		static int pvv1() {
			return 92;
		}

		/** Field type: float */
		static int v1_c0_a() {
			return 100;
		}

		/** Field type: float */
		static int v1_c0_r() {
			return 104;
		}

		/** Field type: float */
		static int v1_c0_g() {
			return 108;
		}

		/** Field type: float */
		static int v1_c0_b() {
			return 112;
		}

		/** Field type: float */
		static int v1_c1_a() {
			return 116;
		}

		/** Field type: float */
		static int v1_c1_r() {
			return 120;
		}

		/** Field type: float */
		static int v1_c1_g() {
			return 124;
		}

		/** Field type: float */
		static int v1_c1_b() {
			return 128;
		}

		/** Field type: pointer */
		static int pv2() {
			return 132;
		}

		/** Field type: pointer */
		static int pvt2() {
			return 140;
		}

		/** Field type: pointer */
		static int pvn2() {
			return 148;
		}

		/** Field type: pointer */
		static int pvv2() {
			return 156;
		}

		/** Field type: float */
		static int v2_c0_a() {
			return 164;
		}

		/** Field type: float */
		static int v2_c0_r() {
			return 168;
		}

		/** Field type: float */
		static int v2_c0_g() {
			return 172;
		}

		/** Field type: float */
		static int v2_c0_b() {
			return 176;
		}

		/** Field type: float */
		static int v2_c1_a() {
			return 180;
		}

		/** Field type: float */
		static int v2_c1_r() {
			return 184;
		}

		/** Field type: float */
		static int v2_c1_g() {
			return 188;
		}

		/** Field type: float */
		static int v2_c1_b() {
			return 192;
		}

	}

	static final class polyvert_t {

		private polyvert_t() {
			throw new AssertionError();
		}

		/** Constant: size in bytes of an instance of this type */
		static int SIZEOF() {
			return 64;
		}

	}

	static final class clipfunc_t {

		private clipfunc_t() {
			throw new AssertionError();
		}

		/** Constant: size in bytes of an instance of this type */
		static int SIZEOF() {
			return 32;
		}

	}

	static final class texture_t {

		private texture_t() {
			throw new AssertionError();
		}

	}

}
