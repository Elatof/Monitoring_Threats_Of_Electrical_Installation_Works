import { Component } from "react";
import { ReactBingmaps } from 'react-bingmaps';
import ReactLoading from 'react-loading';
import "./MainMap.css"
import '../assets/index.less';
import ReactSlider from "react-slider";
import Cookies from 'universal-cookie';


class MainMap extends Component {

    constructor() {
        super();
        this.state = {
            isLoaded1: false,
            isLoaded2: false,
            polygons: [],
            pins: [],
            jumpValue: 0
        };
    }
    


    componentDidMount() {
        let cookie = new Cookies();
        let initialPolygons = [];
        fetch(`http://localhost:8080/service-api/weather/all/polygons?jumpTime=` + this.state.jumpValue,
            {
                headers: {
                    "Authorization": "Bearer " + cookie.get('token')
                }
            })
            .then(response => { return response.json(); })
            .then(data => {
                initialPolygons = data.map((Item) => { return Item });
                this.setState({ polygons: initialPolygons });
                this.setState({ isLoaded1: true });
            });

        fetch(`http://localhost:8080/service-api/weather/lightning/push-pins?jumpTime=` + this.state.jumpValue,
            {
                headers: {
                    "Authorization": "Bearer " + cookie.get('token')
                }
            })
            .then(response => { return response.json(); })
            .then(data => {
                let initialPins = data.map((Item) => { return Item });
                this.setState({ pins: initialPins });
                this.setState({ isLoaded2: true });
            });
    }



    render() {
        function buildJump(value) {
            let result = (value - 3) * 3;
            if (result > 0) {
                return '+' + result;
            }
            if (result < 0) {
                return '- ' + Math.abs(result);
            }
            return '';
        }


        return (
            <div style={{ width: "100vw", height: "80vh" }}>
                <button className="reload" onClick={() => window.location.reload(false)}>Click to reload</button>

                <ReactSlider
                    className="customSlider"
                    trackClassName="customSlider-track"
                    thumbClassName="customSlider-thumb"
                    markClassName="customSlider-mark"
                    marks={1}
                    min={0}
                    max={6}
                    defaultValue={3}
                    renderThumb={(props, state) => <div style={{ alignContent: "center" }} {...props}> {buildJump(state.valueNow)}</div>}
                    onChange={(value) => {
                        this.setState({ jumpValue: value - 3 }, () => {
                            this.componentDidMount();
                        });
                    }}
                />
                <h4 style={{ color: "white" }}>.</h4>
                {this.state.isLoaded1 && this.state.isLoaded2 ? (
                    <>
                        <ReactBingmaps
                            bingmapKey="AoORHdJLECgUf06JJ152s4wWjjyCg5etwyoYBtJvbjUqV-OvknDXKwT9ATzETwPy"
                            center={[48.747111, 32.317217]} //center of Ukraine
                            zoom={5}
                            regularPolygons={this.state.polygons}
                            pushPins={this.state.pins}
                        >
                        </ReactBingmaps>
                    </>
                ) : (
                    <><ReactLoading type="balls" color="#fff" /></>
                )}

            </div>
        );
    }
}
export default MainMap;