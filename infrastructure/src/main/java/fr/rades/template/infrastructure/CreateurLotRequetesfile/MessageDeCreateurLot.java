package fr.rades.template.infrastructure.CreateurLotRequetesfile;

public class MessageDeCreateurLot {
        private final String corps;

        private final String endPoint;

        public MessageDeCreateurLot(String endPoint, String corps) {
            this.corps = corps;
            this.endPoint = endPoint;
        }

        public String getEndPoint() {
            return endPoint ;
        }

        public String getCorps() {
            return corps ;
        }

}
