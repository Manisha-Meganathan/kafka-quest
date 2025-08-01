import { BounceLoader } from 'react-spinners';
interface Loader {
  message: string
}
const Loader = ({ message }: Loader) => {
  return (
    <div>
      (
      <div className="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center z-50">
        <div className="inline-block">
          <BounceLoader color="#3498db" size={60} />
        </div>
        <h1 className="text-lg font-bold text-white ml-2">{message}</h1>
      </div>
      )
    </div>
  );
};

export default Loader;