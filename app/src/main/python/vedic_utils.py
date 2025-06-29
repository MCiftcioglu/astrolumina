import math
from datetime import datetime
import pytz
from skyfield.api import load, Topos
from geopy.geocoders import Nominatim
from timezonefinder import TimezoneFinder

# Vedik (Sidereal) Zodyak Takımyıldızları
VEDIC_SIGNS = [
    "Koç (Mesha)", "Boğa (Vrishabha)", "İkizler (Mithuna)", "Yengeç (Karka)",
    "Aslan (Simha)", "Başak (Kanya)", "Terazi (Tula)", "Akrep (Vrischika)",
    "Yay (Dhanu)", "Oğlak (Makara)", "Kova (Kumbha)", "Balık (Meena)"
]

def get_lahiri_ayanamsa(julian_date):
    """
    Verilen Julian tarihi için Lahiri Ayanamsa'yı hesaplar.
    Bu, tropikal ve sidereal zodyaklar arasındaki farktır.
    """
    # Standart Lahiri formülü (yaklaşık değer)
    # J2000.0 (1 Ocak 2000) tarihindeki Ayanamsa = 23.85 derece
    # Yıllık presesyon (gerileme) yaklaşık 50.29 ark saniye
    days_since_j2000 = julian_date - 2451545.0
    years_since_j2000 = days_since_j2000 / 365.25
    ayanamsa = 23.8566 + (years_since_j2000 * (50.29 / 3600))
    return ayanamsa

def get_sign(longitude):
    """Verilen boylama göre Zodyak burcunu bulur."""
    index = math.floor(longitude / 30)
    return VEDIC_SIGNS[index % 12]

def calculate_vedic_signs(date_str, time_str, city_name):
    """
    Doğum tarihi, saati ve şehrine göre Ay ve Yükselen burcunu hesaplar.
    """
    try:
        # 1. Konumu enlem ve boylama çevir
        geolocator = Nominatim(user_agent="AstroLuminaApp")
        location = geolocator.geocode(city_name)
        if not location:
            return {"error": f"Location '{city_name}' not found."}

        lat, lon = location.latitude, location.longitude

        # 2. Saat dilimini bul
        tf = TimezoneFinder()
        tz_str = tf.timezone_at(lng=lon, lat=lat)
        if not tz_str:
            return {"error": "Could not determine timezone for the location."}
        timezone = pytz.timezone(tz_str)

        # 3. Zamanı ayarla
        ts = load.timescale()
        dt_local = datetime.strptime(f"{date_str} {time_str}", "%d/%m/%Y %H:%M")
        dt_aware = timezone.localize(dt_local)
        t = ts.from_datetime(dt_aware)

        # 4. Gök cisimleri verisini yükle
        eph = load('de421.bsp')
        earth, moon, sun = eph['earth'], eph['moon'], eph['sun']
        
        birth_location = earth + Topos(latitude_degrees=lat, longitude_degrees=lon)

        # 5. Ayanamsa'yı hesapla
        ayanamsa = get_lahiri_ayanamsa(t.jd)

        # 6. Ay Burcunu Hesapla (Chandra Rasi)
        moon_pos = birth_location.at(t).observe(moon).apparent()
        moon_ecliptic_lon = moon_pos.ecliptic_latlon()[1].degrees
        sidereal_moon_lon = (moon_ecliptic_lon - ayanamsa) % 360
        moon_sign = get_sign(sidereal_moon_lon)

        # 7. Yükselen Burcu Hesapla (Lagna)
        # Yerel Sideral Zamanı (LST) hesapla (derece cinsinden)
        # GMST (saat) -> GAST (saat) -> LST (saat) -> LST (derece)
        gast = t.gast
        lst = gast + lon / 15.0 # LST saat cinsinden
        lst_deg = (lst * 15) % 360 # LST derece cinsinden

        # Ekliptik eğikliğini al
        obliquity = earth.at(t).obliquity.degrees
        
        # Yükselen formülü
        obliquity_rad = math.radians(obliquity)
        lst_rad = math.radians(lst_deg)
        
        y = -math.cos(lst_rad)
        x = math.sin(lst_rad) * math.cos(obliquity_rad) - math.tan(math.radians(lat)) * math.sin(obliquity_rad)
        
        tropical_ascendant_lon = math.degrees(math.atan2(y, x))
        sidereal_ascendant_lon = (tropical_ascendant_lon - ayanamsa) % 360
        rising_sign = get_sign(sidereal_ascendant_lon)

        return {
            "moon_sign": moon_sign,
            "rising_sign": rising_sign
        }

    except Exception as e:
        return {"error": str(e)}