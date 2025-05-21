from skyfield.api import load, Topos
from skyfield.positionlib import ICRF
from datetime import datetime
import pytz
import math

def zodiac_sign(degree):
    signs = [
        "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
        "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık"
    ]
    return signs[int(degree / 30) % 12]

def calculate_signs(date_str, time_str, latitude, longitude):
    try:
        dt = datetime.strptime(f"{date_str} {time_str}", "%d/%m/%Y %H:%M")
        dt = pytz.utc.localize(dt)

        planets = load('de421.bsp')
        ts = load.timescale()
        t = ts.utc(dt.year, dt.month, dt.day, dt.hour, dt.minute)

        observer = Topos(latitude_degrees=latitude, longitude_degrees=longitude)

        # Güneş ve Ay
        sun_pos = planets["sun"].at(t).observe(planets["earth"] + observer).apparent()
        moon_pos = planets["moon"].at(t).observe(planets["earth"] + observer).apparent()

        sun_ecliptic = sun_pos.ecliptic_latlon()[1].degrees
        moon_ecliptic = moon_pos.ecliptic_latlon()[1].degrees

        sun_sign = zodiac_sign(sun_ecliptic)
        moon_sign = zodiac_sign(moon_ecliptic)

        # Yükselen burç hesaplaması (doğu ufkunda yükselen nokta)
        earth = planets["earth"]
        astrometric = earth + observer

        # Yeryüzünden doğuya bakış yönü için referans yıldız (sabit nokta)
        e = astrometric.at(t)
        ra, dec, distance = e.radec()

        lst = e.observer_longitude_degrees + 15 * dt.hour + dt.minute / 4.0  # yaklaşık Yerel Saat
        lst = lst % 360

        asc_degree = lst
        asc_sign = zodiac_sign(asc_degree)

        return {
            "sun": sun_sign,
            "moon": moon_sign,
            "ascendant": asc_sign
        }

    except Exception as e:
        return {
            "sun": "Bilinmiyor",
            "moon": "Bilinmiyor",
            "ascendant": "Bilinmiyor"
        }
