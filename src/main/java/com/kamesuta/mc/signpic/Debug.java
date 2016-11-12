package com.kamesuta.mc.signpic;

import org.apache.commons.lang3.StringUtils;

public class Debug {
	public static void main(final String[] args) throws Exception {
		// Reference.logger.info(ImageSizes.LIMIT.size(32, 24, 10, 12));
		//		final IPositionAbsolute abs1 = new PositionAbsolute(0, 0, 0, 0, 100, 100);
		//		final GuiPosition gp = GuiPosition.createBase(abs1)
		//				.child(new RelativePosition(10, 10, -11, -11, true))
		//				.child(new RelativeSizedPosition(0, 0, 21, 21, false));
		//		Reference.logger.info(gp.getAbsolute());

		//		final int p_73734_4_ = -16777216;
		//		final float f3 = (p_73734_4_ >> 24 & 255) / 255.0F;
		//		final float f = (p_73734_4_ >> 16 & 255) / 255.0F;
		//		final float f1 = (p_73734_4_ >> 8 & 255) / 255.0F;
		//		final float f2 = (p_73734_4_ & 255) / 255.0F;
		//		Reference.logger.info(f);
		//		Reference.logger.info(f1);
		//		Reference.logger.info(f2);
		//		Reference.logger.info(f3);

		//		final Pattern p = Pattern.compile("(?:([^\\dx]?)(\\d*x\\d*|\\d*))+?");
		//		final Matcher m = p.matcher("7x7v5j3k4x3vx3");
		//		final Map<String, String> map = new HashMap<String, String>();
		//		while(m.find()){
		//			if (2 <= m.groupCount()) {
		//				final String key = m.group(1);
		//				final String value = m.group(2);
		//				if (!key.isEmpty() || !value.isEmpty())
		//					map.put(key, value);
		//			}
		//		}
		//		Reference.logger.info(map);

		//		final String s = "123456789abcdef123456789abcdef123456789abcdef123456789abcdef123456789abcdef";
		//		final String[] ids = new String[4];
		//		toStrings(ids, s);
		//		Reference.logger.info(isPlaceable(s));
		//		Reference.logger.info(ArrayUtils.toString(ids));
		//
		//		PostData();

		final int color = 0xffffff;
		final float a = (color>>24&255)/255.0F;
		final float r = (color>>16&255)/255.0F;
		final float g = (color>>8&255)/255.0F;
		final float b = (color&255)/255.0F;
		Reference.logger.info(String.format("R:%.04f G:%.04f B:%.04f A:%.04f", r, g, b, a));
	}

	static void toStrings(final String[] sign, final String id) {
		final int length = StringUtils.length(id);
		for (int i = 0; i<4; i++)
			sign[i] = StringUtils.substring(id, 15*i, Math.min(15*(i+1), length));
	}

	static String convertStreamToString(final java.io.InputStream is) {
		final java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	static boolean isPlaceable(final String entryId) {
		return StringUtils.length(entryId)<=15*4;
	}

	// TODO: Fix and test this method.
	//	static void PostData() throws Exception {
	//		final String url = "https://upload.gyazo.com/api/upload";
	//		final HttpClient httpclient = new Downloader().client;
	//
	//		// create the post request.
	//		final HttpPost httppost = new HttpPost(url);
	//		final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	//
	//		final File f = new File("./src/main/resources/assets/signpic/textures/logo.png");
	//		builder.addBinaryBody("imagedata", f, ContentType.DEFAULT_BINARY, f.getName());
	//		builder.addTextBody("access_token", "4d080e95be741beba0b74653a872668326a79526784d2daed9190dc584bffad7");
	//		httppost.setEntity(builder.build());
	//
	//		// execute request
	//		final HttpResponse response = httpclient.execute(httppost);
	//		final HttpEntity resEntity = response.getEntity();
	//		final InputStream stream = resEntity.getContent();
	//
	//		System.out.println(response.getStatusLine());
	//		System.out.println(convertStreamToString(stream));
	//
	//	}
}
