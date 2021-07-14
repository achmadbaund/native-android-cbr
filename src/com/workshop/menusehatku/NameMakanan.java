package com.workshop.menusehatku;

public class NameMakanan {
		private int _id;
		private String nama_paket, waktu_makan;
		private double total_kalori, jarak;
		
		public double getTotalKalori(){
			return total_kalori;
		}
		
		public void setTotalKalori(double total_kalori){
			this.total_kalori = total_kalori;
		}	
		
		public double getJarak(){
			return jarak;
		}
		
		public void setJarak(double jarak){
			this.jarak = jarak;
		}
	
		public int getId(){
			return _id;
		}
		
		public void setId(int _id){
			this._id = _id;
		}
		
		public String getNamaPaket() {
			return nama_paket;
		}

		public void setNamaPaket(String nama_paket) {
			this.nama_paket = nama_paket;
		}

		public String getWaktuMakan() {
			return waktu_makan;
		}

		public void setWaktuMakan(String waktu_makan) {
			this.waktu_makan = waktu_makan;
		}
}
